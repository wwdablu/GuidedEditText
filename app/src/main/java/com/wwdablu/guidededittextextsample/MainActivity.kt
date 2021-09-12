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

        setContentView(mBinding.root)
    }

    inner class RuleDefinitionMinimumLength : RuleDefinition {
        override fun follows(input: String, rule: Rule): RuleDefinition.State {
            return when(input.trim().length) {
                in 0..5 -> RuleDefinition.State.Unsatisfied
                in 5..7 -> RuleDefinition.State.PartiallySatisfied
                else -> RuleDefinition.State.Satisfied
            }
        }

        override fun text(state: RuleDefinition.State): SpannableString {
            return when(state) {
                RuleDefinition.State.Satisfied -> SpannableString("Very good!")
                RuleDefinition.State.Unsatisfied -> SpannableString("Minimum length needed is 8")
                RuleDefinition.State.PartiallySatisfied -> SpannableString("Weak, not accepted")
                RuleDefinition.State.PendingValidation -> SpannableString("Waiting...")
            }
        }
    }
}