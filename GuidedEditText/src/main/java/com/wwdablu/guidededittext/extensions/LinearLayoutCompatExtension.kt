package com.wwdablu.guidededittext.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat


fun LinearLayoutCompat.hide(onHidden: () -> Unit) {
    val anim = ValueAnimator.ofInt(measuredHeight, 0)
    anim.addUpdateListener { valueAnimator ->
        val value = valueAnimator.animatedValue as Int
        val layoutParams: ViewGroup.LayoutParams = layoutParams
        layoutParams.height = value
        setLayoutParams(layoutParams)
    }
    anim.addListener(object: AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            onHidden()
        }
    })
    anim.duration = 500
    anim.start()
}

fun LinearLayoutCompat.show(onHidden: () -> Unit) {
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
                layoutParams = LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
            }
            onHidden()
        }
    })
    anim.duration = 500
    anim.start()
}