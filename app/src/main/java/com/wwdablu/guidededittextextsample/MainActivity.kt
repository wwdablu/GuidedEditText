package com.wwdablu.guidededittextextsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wwdablu.guidededittextextsample.databinding.ActivityMainBinding
import com.wwdablu.guidededittextextsample.rules.PasswordRules
import com.wwdablu.guidededittextextsample.rules.UsernameRules

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        mBinding.textInputField.addRule(UsernameRules.getRules())
        mBinding.passwordInputField.addRule(PasswordRules.getRules())

        setContentView(mBinding.root)
    }
}