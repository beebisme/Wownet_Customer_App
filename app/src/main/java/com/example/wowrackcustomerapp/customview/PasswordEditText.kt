package com.example.wowrackcustomerapp.customview


import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.example.wowrackcustomerapp.R

@SuppressLint("ClickableViewAccessibility")
class PasswordEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    private var isPasswordVisible = false

    init {
        setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0)
        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= (right - drawableEnd.bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
        setupPasswordValidation()
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        transformationMethod = if (isPasswordVisible) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
    }

    private fun setupPasswordValidation() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not implemented
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    showPasswordError()
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not implemented
            }
        })
    }

    private fun showPasswordError() {
        error = "Password must be at least 8 characters long"
    }
}