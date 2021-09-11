package com.wwdablu.guidededittextext

import android.animation.Animator
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation

fun View.spToPixel(sp: Float) : Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}

fun View.dipToPixel(dip: Float) : Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics)
}

fun View.fadeOutAndRemoveFrom(viewGroup: ViewGroup) {
    val view = this
    animate()
        .setListener(object : Animation.AnimationListener, Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animation?) {
                //
            }

            override fun onAnimationEnd(p0: Animation?) {
                //
            }

            override fun onAnimationRepeat(p0: Animation?) {
                //
            }

            override fun onAnimationStart(p0: Animator?) {
                //
            }

            override fun onAnimationEnd(p0: Animator?) {
                viewGroup.removeView(view)
                view.alpha = 1f
            }

            override fun onAnimationCancel(p0: Animator?) {
                //
            }

            override fun onAnimationRepeat(p0: Animator?) {
                //
            }
        })
        .alpha(0f)
        .setDuration(500)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .start()
}

fun View.addAndFadeIn(viewGroup: ViewGroup) {
    alpha = 0f
    val view = this
    viewGroup.addView(this)
    animate()
        .setListener(object : Animation.AnimationListener, Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animation?) {
                //
            }

            override fun onAnimationEnd(p0: Animation?) {
                //
            }

            override fun onAnimationRepeat(p0: Animation?) {
                //
            }

            override fun onAnimationStart(p0: Animator?) {
                //
            }

            override fun onAnimationEnd(p0: Animator?) {
                view.alpha = 1f
            }

            override fun onAnimationCancel(p0: Animator?) {
                //
            }

            override fun onAnimationRepeat(p0: Animator?) {
                //
            }
        })
        .alpha(1f)
        .setDuration(500)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .start()
}