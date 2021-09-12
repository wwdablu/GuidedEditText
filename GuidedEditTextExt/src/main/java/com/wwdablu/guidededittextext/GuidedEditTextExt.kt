package com.wwdablu.guidededittextext

import android.content.Context
import android.graphics.Color
import android.text.InputType.*
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.wwdablu.guidededittextext.abstracts.TextChangeListener
import com.wwdablu.guidededittextext.extensions.addTo
import com.wwdablu.guidededittextext.extensions.dipToPixel
import com.wwdablu.guidededittextext.extensions.reviewFrom
import com.wwdablu.guidededittextext.extensions.setPropertiesByState
import com.wwdablu.guidededittextext.model.RuleView
import java.util.*


class GuidedEditTextExt(context: Context, attrs: AttributeSet) : LinearLayoutCompat(context, attrs) {

    private val inputEditText: AppCompatEditText = AppCompatEditText(context)
    private val rulesContainer: LinearLayoutCompat = LinearLayoutCompat(context)

    private val ruleViewList = LinkedList<RuleView>()
    private var animate: Boolean = true
    private var hideRuleOnSatisfied = true
    private var guideTextSize: Float

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
        notifyRuleSetChange()
    }

    fun notifyRuleChange(rule: Rule, state: RuleDefinition.State) {
        if(rule.lastState != RuleDefinition.State.PendingValidation && state == RuleDefinition.State.PendingValidation) {
            return
        }

        rule.lastState = state
        val ruleView: RuleView? = ruleViewList.find {
            it.rule == rule
        }

        ruleView?.let {
            setRuleProperties(ruleView)
        }
    }

    fun allRulesSatisfied() : Boolean {

        for(ruleView: RuleView in ruleViewList) {
            if(ruleView.rule.lastState != RuleDefinition.State.Satisfied) {
                return false
            }
        }
        return true
    }

    fun hasPartiallySatisfiedRule() : Boolean {

        for(ruleView: RuleView in ruleViewList) {
            if(ruleView.rule.lastState == RuleDefinition.State.PartiallySatisfied) {
                return true
            }
        }
        return false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        inputEditText.apply {
            removeTextChangedListener(textChangeLister)
        }
    }

    private val textChangeLister = object: TextChangeListener() {
        override fun onTextChanged(updatedText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            handleTextChange(updatedText.toString())
        }
    }

    private fun handleTextChange(updatedText: String) {

        for(ruleView in ruleViewList) {
            if(ruleView.rule.notifyMode == RuleDefinition.Notify.Change) {

                ruleView.rule.lastState = ruleView.rule.ruleImpl.follows(updatedText, ruleView.rule)
                setRuleProperties(ruleView)
            }
        }
    }

    private fun setRuleProperties(ruleView: RuleView) {
        ruleView.view.setPropertiesByState(ruleView.rule.lastState, ruleView.rule)

        when(ruleView.rule.lastState) {
            RuleDefinition.State.Satisfied -> {

                /* If the rule has been satisfied check if it needs to be hidden or
                 * should it continue to be displayed
                 */
                if(hideRuleOnSatisfied) {
                    ruleView.view.reviewFrom(rulesContainer, animate)
                    ruleView.viewIsAdded = false
                }
            }

            RuleDefinition.State.PendingValidation,
            RuleDefinition.State.PartiallySatisfied,
            RuleDefinition.State.Unsatisfied -> {
                if(!ruleView.viewIsAdded) {

                    ruleView.view.addTo(rulesContainer, animate)
                    ruleView.viewIsAdded = true
                }
            }
        }
    }

    init {
        orientation = VERTICAL

        val attributes = context.theme.obtainStyledAttributes(attrs,
            R.styleable.GuidedEditTextExt, 0, 0).run {

            animate = getBoolean(R.styleable.GuidedEditTextExt_guideAnimate, true)
            hideRuleOnSatisfied = getBoolean(R.styleable.GuidedEditTextExt_guideTextHideOnRuleSatisfied, true)

            guideTextSize = getDimension(R.styleable.GuidedEditTextExt_guideTextSize, 8f)

                inputEditText.setBackgroundResource(getResourceId(
                    R.styleable.GuidedEditTextExt_inputBackground, R.drawable.rounded_corner
                ))

            rulesContainer.setBackgroundResource(getResourceId(
                R.styleable.GuidedEditTextExt_guideBackgroundImage, R.drawable.guide_background
            ))

            this
        }

        inputEditText.apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            addTextChangedListener(textChangeLister)
            id = generateViewId()
            textSize = attributes.getDimension(R.styleable.GuidedEditTextExt_inputTextSize, 8f)

            inputType = when(attributes.getInt(R.styleable.GuidedEditTextExt_inputType, 1)) {
                InputType.Number.ordinal -> TYPE_CLASS_NUMBER
                InputType.Text.ordinal -> TYPE_CLASS_TEXT
                InputType.Password.ordinal -> {
                    setSingleLine()
                    transformationMethod = PasswordTransformationMethod.getInstance()
                    TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
                InputType.Phone.ordinal -> TYPE_CLASS_PHONE
                else -> TYPE_CLASS_TEXT
            }
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

        addView(inputEditText)
        addView(rulesContainer)
        attributes.recycle()
    }

    private fun createRuleView(rule: Rule) : AppCompatTextView {

        return AppCompatTextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPadding(
                dipToPixel(8f).toInt(),
                0,
                dipToPixel(8f).toInt(),
                0
            )
            id = generateViewId()
            text = rule.ruleImpl.text(RuleDefinition.State.Unsatisfied)
            textSize = guideTextSize
            setTextColor(rule.stateTextColorMap[RuleDefinition.State.Unsatisfied] ?: Color.RED)
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
}