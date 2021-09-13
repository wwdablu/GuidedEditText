package com.wwdablu.guidededittextext

import android.graphics.Color
import androidx.annotation.ColorInt
import com.wwdablu.guidededittextext.RuleDefinition.Notify
import com.wwdablu.guidededittextext.RuleDefinition.State
import kotlinx.coroutines.Job
import java.util.concurrent.atomic.AtomicBoolean

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