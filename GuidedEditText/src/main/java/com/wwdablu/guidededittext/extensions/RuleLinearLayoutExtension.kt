package com.wwdablu.guidededittext.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.util.Log
import android.view.ViewGroup
import com.wwdablu.guidededittext.customui.RuleLinearLayout


internal fun RuleLinearLayout.hide(onHidden: () -> Unit) {
    val anim = ValueAnimator.ofInt(measuredHeight, 0)
    anim.addUpdateListener { valueAnimator ->
        val value = valueAnimator.animatedValue as Int
        val layoutParams: ViewGroup.LayoutParams = layoutParams
        layoutParams.height = value
        setLayoutParams(layoutParams)
    }
    anim.addListener(object: AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            Log.e("App", "onAnimationEnd has been called")
            onHidden()
        }
    })
    anim.duration = 500
    anim.start()
}

internal fun RuleLinearLayout.show(onHidden: () -> Unit) {
    val llView = this
    val anim = ValueAnimator.ofInt(0, 100)
    anim.addUpdateListener { valueAnimator ->
        val value = valueAnimator.animatedValue as Int
        val layoutParams: ViewGroup.LayoutParams = layoutParams
        layoutParams.height = value
        setLayoutParams(layoutParams)
    }
    anim.addListener(object: AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            llView.apply {
                layoutParams = llView.getActualLayoutParam()
            }
            onHidden()
        }
    })
    anim.duration = 500
    anim.start()
}