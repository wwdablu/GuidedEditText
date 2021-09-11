package com.wwdablu.guidededittextext

import android.text.SpannableString

interface IRule {

    enum class State {
        Satisfied,
        Unsatisfied,
        PartiallySatisfied
    }

    enum class Notify {
        Change,
        Finish
    }

    /**
     * Specifies whether the rules has been followed or not.
     *
     * @param input Text entered by the user
     * @return State of the rule
     */
    fun follows(input: String) : State

    /**
     * Provide the SpannableString which will be used by the library to show the text as
     * part of the rule
     */
    fun text(state: State) : SpannableString
}