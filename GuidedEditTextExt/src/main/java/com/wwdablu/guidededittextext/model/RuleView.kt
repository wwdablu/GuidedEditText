package com.wwdablu.guidededittextext.model

import androidx.appcompat.widget.AppCompatTextView
import com.wwdablu.guidededittextext.Rule

internal data class RuleView(
    val rule: Rule,
    val view: AppCompatTextView,
    var viewIsAdded: Boolean = true
)
