package com.wwdablu.guidededittextext.abstracts

import android.text.Editable
import android.text.TextWatcher

abstract class TextChangeListener : TextWatcher {
    override fun beforeTextChanged(updatedText: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //
    }

    override fun onTextChanged(updatedText: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //
    }

    override fun afterTextChanged(p0: Editable?) {
        //
    }
}