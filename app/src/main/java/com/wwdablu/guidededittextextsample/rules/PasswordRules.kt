package com.wwdablu.guidededittextextsample.rules

import android.graphics.Color
import android.text.SpannableString
import com.wwdablu.guidededittext.Rule
import com.wwdablu.guidededittext.RuleDefinition
import java.util.regex.Pattern

class PasswordRules {

    class PasswordMinimumLength : RuleDefinition {
        override fun follows(input: String, rule: Rule): RuleDefinition.State {
            return if(input.trim().length <= 7)
                RuleDefinition.State.Unsatisfied
            else
                RuleDefinition.State.Satisfied
        }

        override fun text(state: RuleDefinition.State): SpannableString {
            return if(state == RuleDefinition.State.Satisfied)
                SpannableString("Password is of minimum length")
            else
                SpannableString("Password must be of 8 characters")
        }
    }

    class PasswordMaximumLength : RuleDefinition {
        override fun follows(input: String, rule: Rule): RuleDefinition.State {
            return if(input.trim().length <= 15)
                RuleDefinition.State.Satisfied
            else
                RuleDefinition.State.Unsatisfied
        }

        override fun text(state: RuleDefinition.State): SpannableString {
            return if(state == RuleDefinition.State.Satisfied)
                SpannableString("Password is of acceptable length")
            else
                SpannableString("Password must be less than 16 characters")
        }
    }

    class PasswordContainsUpperCase : RuleDefinition {

        override fun follows(input: String, rule: Rule): RuleDefinition.State {
            return if(Pattern.compile("^(?=.*[A-Z]).+\$").matcher(input).matches())
                RuleDefinition.State.Satisfied
            else
                RuleDefinition.State.Unsatisfied
        }

        override fun text(state: RuleDefinition.State): SpannableString {
            return if(state == RuleDefinition.State.Satisfied)
                SpannableString("Password contains upper case character")
            else
                SpannableString("Password must contain upper case character")
        }
    }

    class PasswordContainsLowerCase : RuleDefinition {

        override fun follows(input: String, rule: Rule): RuleDefinition.State {
            return if(Pattern.compile("^(?=.*[a-z]).+\$").matcher(input).matches())
                RuleDefinition.State.Satisfied
            else
                RuleDefinition.State.Unsatisfied
        }

        override fun text(state: RuleDefinition.State): SpannableString {
            return if(state == RuleDefinition.State.Satisfied)
                SpannableString("Password contains lower case character")
            else
                SpannableString("Password must contain lower case character")
        }
    }

    class PasswordContainsNumber : RuleDefinition {

        override fun follows(input: String, rule: Rule): RuleDefinition.State {
            return if(Pattern.compile("^(?=.*\\d).+\$").matcher(input).matches())
                RuleDefinition.State.Satisfied
            else
                RuleDefinition.State.Unsatisfied
        }

        override fun text(state: RuleDefinition.State): SpannableString {
            return if(state == RuleDefinition.State.Satisfied)
                SpannableString("Password contains numeric character")
            else
                SpannableString("Password must contain numeric character")
        }
    }

    companion object {

        fun getRules() : List<Rule> {

            val pwdLength = Rule(PasswordMinimumLength())
                .setNotifyOn(RuleDefinition.Notify.Change)
                .setStateText(RuleDefinition.State.Satisfied, Color.parseColor("#f4f4f2"))
                .setStateText(RuleDefinition.State.Unsatisfied, Color.parseColor("#f4f4f2"))

            return listOf(
                pwdLength,
                Rule.similar(pwdLength, PasswordMaximumLength()),
                Rule.similar(pwdLength, PasswordContainsUpperCase()),
                Rule.similar(pwdLength, PasswordContainsLowerCase()),
                Rule.similar(pwdLength, PasswordContainsNumber())
            )
        }
    }
}