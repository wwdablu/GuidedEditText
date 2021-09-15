# GuidedEditText
UI component that can be attached to an EditText view which shows guides(rules) which are needed to be satisfied to have a proper entry.

**__Usage__**:  
//Jitpack statement goes here  

Inside the layout XML an entry for GuidedEditText needs to be created. This control needs to be linked with a EditText view. Internally any text change in the EditText view will trigger the implementation logic (or Rules) provided. It needs to be taken care that the EditText and the GuidedEditText should be present in the same viewgroup.  

```
<EditText
        android:id="@+id/et_password"
        android:layout_width="match_parent"
        android:layout_height="36dp"
        android:background="@drawable/rounded_corner"
        android:layout_marginStart="16dp"
        android:layout_marginEnd="16dp"
        android:paddingStart="8dp"
        android:paddingEnd="8dp"
        android:inputType="textPassword"
        />

<com.wwdablu.guidededittext.GuidedEditText
    android:id="@+id/password_input_field"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="16dp"
    android:layout_marginStart="16dp"
    android:layout_marginEnd="16dp"
    android:textColor="@color/black"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"

    app:guideAnimate="true"
    app:guideTextSize="6sp"
    app:guideBackgroundImage="@drawable/guide_background_sample"
    app:guideLinkedWith="@id/et_password"
    />
```  
Once the layout has been defined, we need to define the Rules. An example of creating a rule:  
```
class UserNameIsEmail : RuleDefinition {
    override fun follows(input: String, rule: Rule): RuleDefinition.State {
        return if(Patterns.EMAIL_ADDRESS.matcher(input).matches())
            RuleDefinition.State.Satisfied
        else
            RuleDefinition.State.Unsatisfied
    }

    override fun text(state: RuleDefinition.State): SpannableString {
        return if(state == RuleDefinition.State.Satisfied)
            SpannableString("Username is acceptable")
        else
            SpannableString("Username must be an email address")
    }
}
```  
A Rule is an implementation of the ```RuleDefinition``` interface. The method ```follows``` implements the logic to check if the Rule is satisfied based on the text entered by the user. If yes, ```State.Satisfied``` can be returned otherwise the appropriate state can be returned. The other method is ```text``` which queries the text to be displayed based on the state returned by ```follows```. The state is internally handled by the Rule.  
  
To create a rule, we need to create an object of the ```Rule``` class and pass the ```RuleDefinition```. For example:  
```
val userNameIsEmail = Rule(UserNameIsEmail())
  .setNotifyOn(RuleDefinition.Notify.Change)
  .setStateText(RuleDefinition.State.Satisfied, Color.parseColor("#f4f4f2"))
  .setStateText(RuleDefinition.State.Unsatisfied, Color.parseColor("#f4f4f2"))
```  
Here the properties of the Rule can be created like the text colours based on the state. Also the imporant part as to the different types of Notify.  
(1) Change --- This signifies that the ```follows``` method will be called as soon as a change is detected in the EditText  
(2) Debounce --- This signifies that the ```follows``` method will be called after some interval, which can be passed when calling ```setNotifyOn```. The default value is 1000ms.  
  
Once this is done, finally the rule is required to be added to the GuidedEditText control.  
```
mBinding.guidedEditText.addRule(userNameIsEmail)
```  
