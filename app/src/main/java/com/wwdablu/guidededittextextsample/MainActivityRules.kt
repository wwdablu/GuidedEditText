package com.wwdablu.guidededittextextsample

import android.graphics.Color
import android.text.SpannableString
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.wwdablu.guidededittext.GuidedEditText
import com.wwdablu.guidededittext.Rule
import com.wwdablu.guidededittext.RuleDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

abstract class MainActivityRules : AppCompatActivity() {

    protected lateinit var guidedUsername: GuidedEditText
    protected lateinit var guidedPassword: GuidedEditText

    fun getUsernameRules() : List<Rule> {

        val userNameIsEmail = Rule(UserNameIsEmail())
            .setNotifyOn(RuleDefinition.Notify.Change)
            .setStateText(RuleDefinition.State.Satisfied, Color.parseColor("#f4f4f2"))
            .setStateText(RuleDefinition.State.Unsatisfied, Color.parseColor("#f4f4f2"))

        val unique = Rule.similar(userNameIsEmail, UsernameMustBeUnique())
            .setStateText(RuleDefinition.State.PendingValidation, Color.parseColor("#f4f4f2"))
            .setNotifyOn(RuleDefinition.Notify.Debounce, 1000)

        return listOf(userNameIsEmail, unique)
    }

    fun getPasswordRules() : List<Rule> {

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

    inner class UsernameMustBeUnique : RuleDefinition {

        private lateinit var pendingRule: Rule
        private var resultString: String = "Ensure username is unique"

        private var checkCount = 0

        override fun follows(input: String, rule: Rule): RuleDefinition.State {

            if(input.isBlank() || input.isEmpty()) {
                return RuleDefinition.State.Unsatisfied
            }

            pendingRule = rule
            resultString = "Checking if username is already taken"

            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                if(++checkCount == 2) {
                    resultString = "Username name is unique"
                    guidedUsername.notifyRuleChange(pendingRule, RuleDefinition.State.Satisfied)
                } else {
                    resultString = "Username is already taken"
                    guidedUsername.notifyRuleChange(pendingRule, RuleDefinition.State.Unsatisfied)
                }
            }
            return RuleDefinition.State.PendingValidation
        }

        override fun text(state: RuleDefinition.State): SpannableString {
            return SpannableString(resultString)
        }
    }

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
}