package com.wwdablu.guidededittext.model

import androidx.appcompat.widget.AppCompatTextView
import com.wwdablu.guidededittext.Rule

internal data class RuleView(
    val rule: Rule,
    val view: AppCompatTextView,
    var viewIsAdded: Boolean = true
)
