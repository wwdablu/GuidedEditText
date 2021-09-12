package com.wwdablu.guidededittextext

import android.graphics.Color
import androidx.annotation.ColorInt
import com.wwdablu.guidededittextext.RuleDefinition.Notify
import com.wwdablu.guidededittextext.RuleDefinition.State

class Rule(ruleDefinition: RuleDefinition) {

    internal val ruleImpl = ruleDefinition

    internal var notifyMode = Notify.Finish
    internal var stateTextColorMap = HashMap<State, @ColorInt Int>()
    internal var lastState: State = State.Unsatisfied

    init {
        stateTextColorMap.apply {
            put(State.Satisfied, Color.GREEN)
            put(State.Unsatisfied, Color.RED)
            put(State.PartiallySatisfied, Color.YELLOW)
            put(State.PendingValidation, Color.DKGRAY)
        }
    }

    fun setNotifyOn(notify: Notify) : Rule {
        notifyMode = notify
        return this
    }

    fun setStateText(state: State, @ColorInt textColor: Int) : Rule {
        stateTextColorMap[state] = textColor
        return this
    }

    companion object {

        fun similar(rule: Rule, ruleDefinition: RuleDefinition) : Rule {
            return Rule(ruleDefinition).apply {
                for(state: State in rule.stateTextColorMap.keys) {
                    stateTextColorMap[state] = rule.stateTextColorMap[state]!!
                }

                notifyMode = rule.notifyMode
            }
        }
    }
}