package com.example.wowrackcustomerapp.ui.main.section.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.databinding.ActivityDetailTrafficBinding

@Suppress("DEPRECATION")
class DetailTraffic : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTrafficBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTrafficBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}