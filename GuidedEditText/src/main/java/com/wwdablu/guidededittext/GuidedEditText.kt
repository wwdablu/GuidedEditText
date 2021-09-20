package com.wwdablu.guidededittext

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatTextView
import com.wwdablu.guidededittext.abstracts.TextChangeListener
import com.wwdablu.guidededittext.customui.RuleLinearLayout
import com.wwdablu.guidededittext.extensions.dipToPixel
import com.wwdablu.guidededittext.extensions.hide
import com.wwdablu.guidededittext.extensions.setPropertiesByState
import com.wwdablu.guidededittext.extensions.show
import com.wwdablu.guidededittext.model.RuleView
import kotlinx.coroutines.*
import java.util.*


class GuidedEditText(context: Context, attrs: AttributeSet) : RuleLinearLayout(context, attrs) {

    private val rulesContainer = this
    private var hostEditTextResId = 0

    private val ruleViewList = LinkedList<RuleView>()
    private var animate: Boolean = true
    private var hideRuleOnSatisfied = true
    private var guideTextSize: Float

    /**
     * Add a rule that will be applicable
     *
     * @param rules varags of @{link Rule}
     */
    fun addRule(vararg rules: Rule) {
        for(rule: Rule in rules) {
            ruleViewList.add(RuleView(rule, createRuleView(rule)))
        }
        notifyRuleSetChange()
    }

    /**
     * Add a list of rules that will be applicable.
     *
     * @param rules List of rules
     */
    fun addRule(rules: List<Rule>) {
        for(rule: Rule in rules) {
            ruleViewList.add(RuleView(rule, createRuleView(rule)))
        }
        notifyRuleSetChange()
    }

    /**
     * Call when the current state of the Rule is {@link RuleDefinition.State.PendingValidation} to
     * update the updated rule state. Should not be called on a rule whose state is not pending. A
     * rule with PendingValidation state can be again set to PendingValidation
     *
     * @param rule Rule which was in pending validation
     * @param state Updated state.
     */
    fun notifyRuleChange(rule: Rule, state: RuleDefinition.State) {
        if(rule.state != RuleDefinition.State.PendingValidation && state == RuleDefinition.State.PendingValidation) {
            return
        }

        rule.state = state
        val ruleView: RuleView? = ruleViewList.find {
            it.rule == rule
        }

        ruleView?.let {
            setRuleProperties(ruleView)
        }
    }

    /**
     * Verifies if all the rules have been satisfied, i.e they are in @{RuleDefinition.State.Satisfied}
     * state.
     *
     * @return True is all the rules have been satisfied.
     */
    fun allRulesSatisfied() : Boolean {

        for(ruleView: RuleView in ruleViewList) {
            if(ruleView.rule.state != RuleDefinition.State.Satisfied) {
                return false
            }
        }
        return true
    }

    /**
     * Returns list of rules which are unsatisfied.
     *
     * @return List of rules which are unsatisfied
     */
    fun getUnsatisfiedRules() : List<Rule> {

        val list = LinkedList<Rule>()
        for(ruleView: RuleView in ruleViewList) {
            if(ruleView.rule.state == RuleDefinition.State.Unsatisfied) {
                list.add(ruleView.rule)
            }
        }
        return list
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        inputEditText.apply {
            removeTextChangedListener(textChangeLister)
        }
    }

    private val textChangeLister = object: TextChangeListener() {

        override fun beforeTextChanged(updatedText: CharSequence?, p1: Int, p2: Int, p3: Int) {

            for(ruleView in ruleViewList) {
                ruleView.apply {
                    if(rule.notifyMode == RuleDefinition.Notify.Debounce) {

                        if(rule.isDebouncing()) {
                            rule.debounceJob.cancelChildren()
                        }

                        rule.debounceJob = CoroutineScope(Dispatchers.Main).launch {
                            val beforeDebounce = inputEditText.editableText.toString()
                            delay(rule.notifyAfter)
                            if(inputEditText.editableText.toString().contentEquals(beforeDebounce) && isActive) {
                                rule.state = rule.ruleImpl.follows(inputEditText.editableText.toString(), rule)
                                setRuleProperties(ruleView)
                                hideRuleContainerIfApplicable()
                            }
                        }
                    }
                }
            }
        }
        override fun onTextChanged(updatedText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            handleTextChange(updatedText.toString())
        }
    }

    private fun handleTextChange(updatedText: String) {

        for(ruleView in ruleViewList) {

            ruleView.apply {
                if(rule.notifyMode == RuleDefinition.Notify.Change) {
                    rule.state = rule.ruleImpl.follows(updatedText, rule)
                    setRuleProperties(this)
                    hideRuleContainerIfApplicable()
                }
            }
        }
    }

    private fun setRuleProperties(ruleView: RuleView) {
        ruleView.view.setPropertiesByState(ruleView.rule.state, ruleView.rule)

        when(ruleView.rule.state) {
            RuleDefinition.State.Satisfied -> {

                /* If the rule has been satisfied check if it needs to be hidden or
                 * should it continue to be displayed
                 */
                if(hideRuleOnSatisfied && ruleView.viewIsAdded) {
                    rulesContainer.removeView(ruleView.view)
                    ruleView.viewIsAdded = false
                }
            }

            RuleDefinition.State.PendingValidation -> {
                //It will be handled by setPropertiesByState method. No need to do any additional
                //task in here at the moment.
            }

            RuleDefinition.State.PartiallySatisfied,
            RuleDefinition.State.Unsatisfied -> {
                if(!ruleView.viewIsAdded) {
                    rulesContainer.addView(ruleView.view)
                    ruleView.viewIsAdded = true
                }
            }
        }

        hideRuleContainerIfApplicable()
    }

    /* If no rules are visible then do not display the rule container as it would just be an
     * empty view with paddings.
     */
    private val hideRuleContainerIfApplicable = {

        val visibleViewCount = ruleViewList.filter {
            it.viewIsAdded
        }.size

        if(visibleViewCount == 0 && !rulesContainer.isHidden) {
            rulesContainer.hide {
                isHidden = true
            }
        } else if (visibleViewCount != 0 && rulesContainer.isHidden) {
            rulesContainer.isHidden = false
            rulesContainer.show {
                //
            }
        }
    }

    init {

        layoutTransition = LayoutTransition()

        orientation = VERTICAL

        val attributes = context.theme.obtainStyledAttributes(attrs,
            R.styleable.GuidedEditTextExt, 0, 0).apply {

            animate = getBoolean(R.styleable.GuidedEditTextExt_guideAnimate, true)
            hideRuleOnSatisfied = getBoolean(R.styleable.GuidedEditTextExt_guideTextHideOnRuleSatisfied, true)

            hostEditTextResId = getResourceId(R.styleable.GuidedEditTextExt_guideLinkedWith, 0)

            guideTextSize = getDimension(R.styleable.GuidedEditTextExt_guideTextSize, 8f)

            rulesContainer.setBackgroundResource(getResourceId(
                R.styleable.GuidedEditTextExt_guideBackgroundImage, R.drawable.guide_background))
        }

        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                inputEditText = (parent as ViewGroup).findViewById(hostEditTextResId)
                inputEditText.addTextChangedListener(textChangeLister)

                rulesContainer.layoutParams = getActualLayoutParam()

                notifyRuleSetChange()
                return true
            }
        })

        attributes.recycle()
    }

    private fun createRuleView(rule: Rule) : AppCompatTextView {

        return AppCompatTextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPadding(
                dipToPixel(8f).toInt(), 0, dipToPixel(8f).toInt(), 0
            )
            id = generateViewId()
            textSize = guideTextSize
            setTextColor(rule.stateTextColorMap[RuleDefinition.State.Unsatisfied] ?: Color.RED)
        }
    }

    private fun notifyRuleSetChange() {

        //Proceed only in the underlying edit text view has been created
        if(!hasEditText()) {
            return
        }

        rulesContainer.apply {
            removeAllViews()
            for(rulePair in ruleViewList) {

                rulePair.run {

                    //Probe to see if the rule is followed. If so, then we will not add to the view
                    val state = rule.ruleImpl.follows(inputEditText.editableText.toString(), rulePair.rule)
                    rule.state = state
                    view.text = rule.ruleImpl.text(state)
                    setRuleProperties(this)

                    //If rule is unsatisfied or explicitly asked to show satisfied rules, then add
                    if(state == RuleDefinition.State.Unsatisfied || !hideRuleOnSatisfied) {
                        rulesContainer.addView(rulePair.view)
                    }
                }
            }
        }

        hideRuleContainerIfApplicable.invoke()
    }
}