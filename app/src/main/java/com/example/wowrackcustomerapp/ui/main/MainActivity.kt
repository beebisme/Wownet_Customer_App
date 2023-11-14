package com.example.wowrackcustomerapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.databinding.ActivityMainBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.login.LoginActivity
import com.example.wowrackcustomerapp.ui.main.section.HelpFragment
import com.example.wowrackcustomerapp.ui.main.section.HomeFragment
import com.example.wowrackcustomerapp.ui.main.section.ProfileFragment

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
        setupView()
    }
    private fun setupView(){
        loadFragment(HomeFragment())
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.help -> {
                    loadFragment(HelpFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                else -> {
//                    loadFragment(HomeFragment)
                    true
                }
            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.container.id,fragment)
        transaction.commit()
    }
}