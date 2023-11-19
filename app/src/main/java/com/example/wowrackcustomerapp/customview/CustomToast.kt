package com.example.wowrackcustomerapp.customview

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.example.wowrackcustomerapp.R
import java.time.Duration

fun Toast.showCustomToast(message : String, activity : Activity, color: ColorDrawable){
    val layout = activity.layoutInflater.inflate(
        R.layout.custom_toast,
        activity.findViewById(R.id.toast_container)
    )

    layout.background = color
    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = message

    this.apply {
        setGravity(Gravity.BOTTOM, 0, 40)
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}