package com.example.wowrackcustomerapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.wowrackcustomerapp.databinding.ActivityMainBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this){user->
            if (!user.isLogin){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}