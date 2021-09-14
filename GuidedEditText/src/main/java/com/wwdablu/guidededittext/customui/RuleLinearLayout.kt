package com.wwdablu.guidededittext.customui

import android.content.Context
import androidx.appcompat.widget.LinearLayoutCompat
import com.wwdablu.guidededittext.extensions.dipToPixel

internal class RuleLinearLayout(context: Context) : LinearLayoutCompat(context) {

    internal fun getActualLayoutParam() : LayoutParams {
        return LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(dipToPixel(8f).toInt(), 0, dipToPixel(8f).toInt(), 0)
        }
    }
}