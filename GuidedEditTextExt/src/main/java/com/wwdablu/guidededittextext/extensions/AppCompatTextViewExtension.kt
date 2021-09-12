package com.wwdablu.guidededittextext.extensions

import androidx.appcompat.widget.AppCompatTextView
import com.wwdablu.guidededittextext.Rule
import com.wwdablu.guidededittextext.RuleDefinition

fun AppCompatTextView.setPropertiesByState(state: RuleDefinition.State, rule: Rule) {
    setTextColor(rule.stateTextColorMap[state]!!)
    text = rule.ruleImpl.text(state)
}