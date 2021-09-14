package com.wwdablu.guidededittextextsample

import android.os.Bundle
import android.text.SpannableString
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.wwdablu.guidededittext.Rule
import com.wwdablu.guidededittext.RuleDefinition
import com.wwdablu.guidededittextextsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)

        val userNameMinimumLengthRule = Rule(UserNameMinimumLength())
            .setNotifyOn(RuleDefinition.Notify.Change, 1000)

        val userNameIsEmail = Rule.similar(userNameMinimumLengthRule, UserNameIsEmail())
            .setNotifyOn(RuleDefinition.Notify.Debounce)

        mBinding.textInputField.addRule(userNameMinimumLengthRule, userNameIsEmail)

        setContentView(mBinding.root)
    }

    inner class UserNameMinimumLength : RuleDefinition {
        override fun follows(input: String, rule: Rule): RuleDefinition.State {
            return if(input.trim().length <= 7)
                RuleDefinition.State.Unsatisfied
            else
                RuleDefinition.State.Satisfied
        }

        override fun text(state: RuleDefinition.State): SpannableString {
            return if(state == RuleDefinition.State.Satisfied)
                SpannableString("Username is acceptable")
            else
                SpannableString("Username must be of 8 characters")
        }
    }

    inner class UserNameIsEmail : RuleDefinition {
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
}