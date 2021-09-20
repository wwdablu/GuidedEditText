package com.wwdablu.guidededittextextsample

import android.os.Bundle
import com.wwdablu.guidededittextextsample.databinding.ActivityMainBinding

class MainActivity : MainActivityRules() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        guidedUsername = mBinding.guideUsername
        guidedPassword = mBinding.guidePassword

        mBinding.guideUsername.addRule(getUsernameRules())
        mBinding.guidePassword.addRule(getPasswordRules())

        setContentView(mBinding.root)
    }
}