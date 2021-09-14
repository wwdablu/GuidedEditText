package com.wwdablu.guidededittext.extensions

import androidx.appcompat.widget.AppCompatTextView
import com.wwdablu.guidededittext.Rule
import com.wwdablu.guidededittext.RuleDefinition

fun AppCompatTextView.setPropertiesByState(state: RuleDefinition.State, rule: Rule) {
    setTextColor(rule.stateTextColorMap[state]!!)
    text = rule.ruleImpl.text(state)
}