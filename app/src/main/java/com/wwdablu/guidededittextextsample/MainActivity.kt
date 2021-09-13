package com.wwdablu.guidededittextextsample

import android.os.Bundle
import android.text.SpannableString
import androidx.appcompat.app.AppCompatActivity
import com.wwdablu.guidededittextext.Rule
import com.wwdablu.guidededittextext.RuleDefinition
import com.wwdablu.guidededittextextsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)

        val userNameMinimumLengthRule = Rule(UserNameMinimumLength())
            .setNotifyOn(RuleDefinition.Notify.Debounce, 3000)

        mBinding.textInputField.addRule(userNameMinimumLengthRule)

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
}