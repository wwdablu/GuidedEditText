package com.wwdablu.guidededittextext

import android.util.TypedValue
import android.view.View

fun View.spToPixel(sp: Float) : Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}

fun View.dipToPixel(dip: Float) : Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics)
}