package com.adam.submissionstoryapp.ui.costumview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.adam.submissionstoryapp.R

class EditTextEmail: AppCompatEditText {
    private lateinit var defaultBackground: Drawable
    private lateinit var errorBackground: Drawable

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init(){
        val errorMassage = context.getString(R.string.email_error)
        defaultBackground = ContextCompat.getDrawable(context, R.drawable.text_container) as Drawable
        errorBackground = ContextCompat.getDrawable(context, R.drawable.text_container_error) as Drawable

        hint = context.getString(R.string.email)
        background = defaultBackground
        addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                background = if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()&&s.toString().isNotEmpty()) {
                    error = errorMassage
                    errorBackground
                } else{
                    defaultBackground
                }
            }

            override fun afterTextChanged(s: Editable) {}

        })
    }

}