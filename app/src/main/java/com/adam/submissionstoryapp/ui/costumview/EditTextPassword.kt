package com.adam.submissionstoryapp.ui.costumview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.adam.submissionstoryapp.R

class EditTextPassword: AppCompatEditText {
    private lateinit var defaultBackground: Drawable
    private lateinit var errorBackground: Drawable

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context,attrs){
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        transformationMethod = PasswordTransformationMethod.getInstance()
    }

    private fun init(){
        defaultBackground = ContextCompat.getDrawable(context, R.drawable.text_container) as Drawable
        errorBackground = ContextCompat.getDrawable(context, R.drawable.text_container_error) as Drawable

        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        background = defaultBackground
        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                background = if (s.length < 6&&s.isNotEmpty()) {
                    error = context.getString(R.string.password_less_than_6)
                    errorBackground
                }else{
                    defaultBackground
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //Do Nothing
            }

        })
    }

}