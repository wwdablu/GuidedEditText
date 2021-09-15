package com.wwdablu.guidededittext.customui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.wwdablu.guidededittext.extensions.dipToPixel

open class RuleLinearLayout(context: Context, attrs: AttributeSet) : LinearLayoutCompat(context, attrs) {

    internal lateinit var inputEditText: AppCompatEditText

    internal fun hasEditText() : Boolean {
        return this::inputEditText.isInitialized
    }

    internal fun getActualLayoutParam() : LayoutParams {
        return LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(inputEditText.marginStart + dipToPixel(8f).toInt(),
                0, dipToPixel(8f).toInt() + inputEditText.marginEnd, 0)
            setPadding(0, dipToPixel(8f).toInt(), 0, dipToPixel(8f).toInt())
        }
    }
}