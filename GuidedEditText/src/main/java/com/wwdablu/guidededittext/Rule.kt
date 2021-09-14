package com.wwdablu.guidededittext

import android.graphics.Color
import androidx.annotation.ColorInt
import com.wwdablu.guidededittext.RuleDefinition.Notify
import com.wwdablu.guidededittext.RuleDefinition.State
import kotlinx.coroutines.Job

class Rule(ruleDefinition: RuleDefinition) {

    internal val ruleImpl = ruleDefinition

    internal var notifyMode = Notify.Debounce
    internal var notifyAfter: Long = 600
    internal var stateTextColorMap = HashMap<State, @ColorInt Int>()
    internal var state: State = State.Unsatisfied

    internal lateinit var debounceJob: Job

    init {
        stateTextColorMap.apply {
            put(State.Satisfied, Color.GREEN)
            put(State.Unsatisfied, Color.RED)
            put(State.PartiallySatisfied, Color.YELLOW)
            put(State.PendingValidation, Color.DKGRAY)
        }
    }

    fun setNotifyOn(notify: Notify, notifyAfter: Long = 600) : Rule {
        notifyMode = notify
        this.notifyAfter = notifyAfter
        return this
    }

    fun setStateText(state: State, @ColorInt textColor: Int) : Rule {
        stateTextColorMap[state] = textColor
        return this
    }

    internal fun isDebouncing() : Boolean {
        return this::debounceJob.isInitialized && debounceJob.isActive
    }

    companion object {

        /**
         * Create a Rule object similar to the one provided in the parameter, but a new object to
         * the RuleDefinition needs to be provided. The other parameters will be copied over.
         *
         * @param rule Rule using which a similar new Rule is to be created
         * @param ruleDefinition An implementation for a RuleDefinition
         * @return New rule object
         */
        fun similar(rule: Rule, ruleDefinition: RuleDefinition) : Rule {
            return Rule(ruleDefinition).apply {
                for(state: State in rule.stateTextColorMap.keys) {
                    stateTextColorMap[state] = rule.stateTextColorMap[state]!!
                }

                notifyMode = rule.notifyMode
                notifyAfter = rule.notifyAfter
            }
        }
    }
}