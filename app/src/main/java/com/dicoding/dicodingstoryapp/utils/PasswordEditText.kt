package com.dicoding.dicodingstoryapp.utils

import android.content.Context
import android.util.AttributeSet
import android.text.TextWatcher
import android.text.Editable
import android.util.TypedValue
import com.google.android.material.textfield.TextInputEditText

class PasswordEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    private var errorListener: ((Boolean) -> Unit)? = null
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            validatePassword(s.toString())
        }
    }

    init {
        addTextChangedListener(textWatcher)
        val rightPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            40f,
            resources.displayMetrics
        ).toInt()

        setPadding(
            paddingLeft,
            paddingTop,
            rightPadding,
            paddingBottom
        )
    }

    private fun validatePassword(password: String) {
        if (password.length < 8) {
            error = "Password must be at least 8 characters long"
            errorListener?.invoke(true)
        } else {
            error = null
            errorListener?.invoke(false)
        }
    }

    fun setErrorListener(listener: (Boolean) -> Unit) {
        errorListener = listener
    }
}