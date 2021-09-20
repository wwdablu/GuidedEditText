package com.wwdablu.guidededittext.customui

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import com.wwdablu.guidededittext.extensions.dipToPixel

open class RuleLinearLayout(context: Context, attrs: AttributeSet) : LinearLayoutCompat(context, attrs) {

    internal lateinit var inputEditText: AppCompatEditText
    internal var isHidden: Boolean = false

    internal fun hasEditText() : Boolean {
        return this::inputEditText.isInitialized
    }

    internal fun getActualLayoutParam() : ViewGroup.LayoutParams {

        val lp = layoutParams
        lp.height = LayoutParams.WRAP_CONTENT
        setPadding(0, dipToPixel(8f).toInt(), 0, dipToPixel(8f).toInt())

        return lp
    }
}