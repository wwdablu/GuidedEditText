package com.wwdablu.guidededittextextsample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import com.wwdablu.guidededittextext.GuidedEditTextExt
import com.wwdablu.guidededittextext.IRule
import com.wwdablu.guidededittextext.Rule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rule: Rule = Rule.Builder(RuleMinimumLength())
            .setNotifyOn(IRule.Notify.Change)
            .setStateText(IRule.State.Satisfied, Color.BLACK)
            .setStateText(IRule.State.Unsatisfied, Color.BLUE)
            .setStateText(IRule.State.PartiallySatisfied, Color.DKGRAY)
            .build()

        val rule1: Rule = Rule.Builder(RuleMustHaveNumber())
            .setNotifyOn(IRule.Notify.Change)
            .setStateText(IRule.State.Satisfied, Color.BLACK)
            .setStateText(IRule.State.Unsatisfied, Color.BLUE)
            .setStateText(IRule.State.PartiallySatisfied, Color.DKGRAY)
            .build()

        findViewById<GuidedEditTextExt>(R.id.guidedEditTextExt).addRule(rule, rule1)
    }

    class RuleMinimumLength : IRule {
        override fun follows(input: String): IRule.State {
            return if(input.trim().length >= 8) IRule.State.Satisfied else IRule.State.Unsatisfied
        }

        override fun text(state: IRule.State): SpannableString {
            return when(state) {
                IRule.State.Satisfied -> SpannableString("Very good!")
                IRule.State.Unsatisfied -> SpannableString("Minimum length needed is 8")
                IRule.State.PartiallySatisfied -> SpannableString("Error")
            }
        }
    }

    class RuleMustHaveNumber : IRule {

        override fun follows(input: String): IRule.State {
            return if(input.contains("^(?=.*\\d).+\$")) IRule.State.Satisfied else IRule.State.Unsatisfied
        }

        override fun text(state: IRule.State): SpannableString {
            return when(state) {
                IRule.State.Satisfied -> SpannableString("Very good!")
                IRule.State.Unsatisfied -> SpannableString("Must have atleast 1 number")
                IRule.State.PartiallySatisfied -> SpannableString("Error")
            }
        }
    }
}