package com.wwdablu.guidededittextext.abstracts

import android.animation.Animator
import android.view.animation.Animation

abstract class AnimationStateListener : Animation.AnimationListener, Animator.AnimatorListener {

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
        //
    }

    override fun onAnimationCancel(p0: Animator?) {
        //
    }

    override fun onAnimationRepeat(p0: Animator?) {
        //
    }
}