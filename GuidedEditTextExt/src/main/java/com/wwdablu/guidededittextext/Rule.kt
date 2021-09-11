package com.wwdablu.guidededittextext

import android.graphics.Color
import androidx.annotation.ColorInt
import com.wwdablu.guidededittextext.IRule.Notify
import com.wwdablu.guidededittextext.IRule.State

class Rule private constructor(iRule: IRule) {

    internal val ruleImpl = iRule

    internal fun setStateTextColor(state: State, @ColorInt color: Int) {
        stateTextColorMap[state] = color
    }

    internal var notifyMode = Notify.Finish
    internal var stateTextColorMap = HashMap<State, @ColorInt Int>()

    init {
        stateTextColorMap.apply {
            put(State.Satisfied, Color.GREEN)
            put(State.Unsatisfied, Color.RED)
            put(State.PartiallySatisfied, Color.YELLOW)
        }
    }

    class Builder(ruleImpl: IRule) {

        private val rule: Rule = Rule(ruleImpl)

        fun setNotifyOn(notify: Notify) : Builder {
            rule.notifyMode = notify
            return this
        }

        fun setStateText(state: State, @ColorInt textColor: Int) : Builder {
            rule.stateTextColorMap[state] = textColor
            return this
        }

        fun build() : Rule {
            return rule
        }
    }
}