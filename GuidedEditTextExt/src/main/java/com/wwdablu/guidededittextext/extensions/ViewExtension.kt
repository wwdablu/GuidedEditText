package com.wwdablu.guidededittextext.extensions

import android.animation.Animator
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.wwdablu.guidededittextext.abstracts.AnimationStateListener

fun View.spToPixel(sp: Float) : Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}

fun View.dipToPixel(dip: Float) : Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics)
}

fun View.reviewFrom(viewGroup: ViewGroup, animate: Boolean) {

    if(!animate) {
        viewGroup.removeView(this)
        return
    }

    val view = this
    animate()
        .setListener(object : AnimationStateListener() {
            override fun onAnimationEnd(p0: Animator?) {
                viewGroup.removeView(view)
                view.alpha = 1f
            }
        })
        .alpha(0f)
        .setDuration(500)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .start()
}

fun View.addTo(viewGroup: ViewGroup, animate: Boolean) {

    if(!animate) {
        viewGroup.addView(this)
        return
    }

    alpha = 0f
    val view = this
    viewGroup.addView(this)
    animate()
        .setListener(object : AnimationStateListener() {
            override fun onAnimationEnd(p0: Animator?) {
                view.alpha = 1f
            }
        })
        .alpha(1f)
        .setDuration(500)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .start()
}