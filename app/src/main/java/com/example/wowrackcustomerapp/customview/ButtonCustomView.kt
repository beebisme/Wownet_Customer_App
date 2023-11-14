package com.example.wowrackcustomerapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.wowrackcustomerapp.R

class ButtonCustomView : AppCompatButton {
    private lateinit var backGround: Drawable
    private var txtColor: Int = 0
    private var buttonText: String = ""

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = backGround
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = buttonText
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ButtonCustomView)
            buttonText = typedArray.getString(R.styleable.ButtonCustomView_buttonText) ?: "Login"
            txtColor =
                typedArray.getColor(R.styleable.ButtonCustomView_buttonTextColor, ContextCompat.getColor(context, R.color.white))
            backGround = typedArray.getDrawable(R.styleable.ButtonCustomView_buttonBackground)
                ?: ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
            typedArray.recycle()
        } ?: run {
            txtColor = ContextCompat.getColor(context, R.color.white)
            backGround = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        }
    }
}
