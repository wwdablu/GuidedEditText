package com.wwdablu.guidededittextext

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import java.util.*

class GuidedEditTextExt(context: Context, attrs: AttributeSet) : LinearLayoutCompat(context, attrs) {

    private val inputEditText: AppCompatEditText = AppCompatEditText(context)
    private val rulesContainer: LinearLayoutCompat = LinearLayoutCompat(context)

    private val ruleViewList = LinkedList<RuleView>()

    fun addRule(vararg rules: Rule) {
        for(rule: Rule in rules) {
            ruleViewList.add(RuleView(rule, createRuleView(rule)))
        }
        notifyRuleSetChange()
    }

    fun addRule(rules: List<Rule>) {
        for(rule: Rule in rules) {
            ruleViewList.add(RuleView(rule, createRuleView(rule)))
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        inputEditText.apply {
            removeTextChangedListener(textChangeLister)
        }
        notifyRuleSetChange()
    }

    private val textChangeLister = object: TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //
        }

        override fun onTextChanged(updatedText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            for(ruleView in ruleViewList) {
                if(ruleView.rule.notifyMode == IRule.Notify.Change) {

                    when(ruleView.rule.ruleImpl.follows(updatedText.toString())) {
                        IRule.State.Satisfied -> {
                            rulesContainer.removeView(ruleView.view)
                            ruleView.viewIsAdded = false
                        }
                        IRule.State.Unsatisfied,
                        IRule.State.PartiallySatisfied -> {
                            if(!ruleView.viewIsAdded) {
                                rulesContainer.addView(ruleView.view)
                                ruleView.viewIsAdded = true
                            }
                        }
                    }
                }
            }
        }

        override fun afterTextChanged(p0: Editable?) {
            //
        }
    }

    init {
        orientation = VERTICAL

        inputEditText.apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            addTextChangedListener(textChangeLister)
            id = generateViewId()
        }

        rulesContainer.apply {
            val lpConfig = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            lpConfig.setMargins(dipToPixel(8f).toInt(), 0, dipToPixel(8f).toInt(), 0)
            layoutParams = lpConfig

            setPadding(
                dipToPixel(4f).toInt(),
                dipToPixel(8f).toInt(),
                dipToPixel(4f).toInt(),
                dipToPixel(8f).toInt()
            )

            orientation = VERTICAL
            id = generateViewId()
        }

        this.apply {
            addView(inputEditText)
            addView(rulesContainer)
        }
    }

    private fun createRuleView(rule: Rule) : View {

        return AppCompatTextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            id = generateViewId()
            text = rule.ruleImpl.text(IRule.State.Unsatisfied)
            setTextColor(rule.stateTextColorMap[IRule.State.Unsatisfied] ?: Color.RED)
        }
    }

    private fun notifyRuleSetChange() {
        rulesContainer.apply {
            removeAllViews()
            for(rulePair in ruleViewList) {
                addView(rulePair.view)
            }
        }
    }

    private data class RuleView(
        val rule: Rule,
        val view: View,
        var viewIsAdded: Boolean = true)
}