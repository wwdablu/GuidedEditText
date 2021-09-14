package com.wwdablu.guidededittextextsample.rules

import android.graphics.Color
import android.text.SpannableString
import android.util.Patterns
import com.wwdablu.guidededittext.Rule
import com.wwdablu.guidededittext.RuleDefinition

class UsernameRules {

    class UserNameIsEmail : RuleDefinition {
        override fun follows(input: String, rule: Rule): RuleDefinition.State {
            return if(Patterns.EMAIL_ADDRESS.matcher(input).matches())
                RuleDefinition.State.Satisfied
            else
                RuleDefinition.State.Unsatisfied
        }

        override fun text(state: RuleDefinition.State): SpannableString {
            return if(state == RuleDefinition.State.Satisfied)
                SpannableString("Username is acceptable")
            else
                SpannableString("Username must be an email address")
        }
    }

    companion object {

        fun getRules() : List<Rule> {

            val userNameIsEmail = Rule(UserNameIsEmail())
                .setNotifyOn(RuleDefinition.Notify.Change)
                .setStateText(RuleDefinition.State.Satisfied, Color.parseColor("#f4f4f2"))
                .setStateText(RuleDefinition.State.Unsatisfied, Color.parseColor("#f4f4f2"))

            return listOf(userNameIsEmail)
        }
    }
}