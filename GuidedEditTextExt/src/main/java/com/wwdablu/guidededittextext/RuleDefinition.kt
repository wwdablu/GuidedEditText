package com.wwdablu.guidededittextext

import android.text.SpannableString

interface RuleDefinition {

    enum class State {
        Satisfied,
        Unsatisfied,
        PartiallySatisfied,
        PendingValidation
    }

    enum class Notify {
        Change,
        Debounce
    }

    /**
     * Specifies whether the rules has been followed or not.
     *
     * @param input Text entered by the user
     * @param rule Rule for which the test is being done
     * @return State of the rule
     */
    fun follows(input: String, rule: Rule) : State

    /**
     * Provide the SpannableString which will be used by the library to show the text as
     * part of the rule
     */
    fun text(state: State) : SpannableString
}