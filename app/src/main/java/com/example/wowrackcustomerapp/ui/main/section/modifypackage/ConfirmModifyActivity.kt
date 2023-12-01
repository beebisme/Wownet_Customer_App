package com.example.wowrackcustomerapp.ui.main.section.modifypackage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.databinding.ActivityConfirmModifyBinding

class ConfirmModifyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityConfirmModifyBinding = ActivityConfirmModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
    }
}