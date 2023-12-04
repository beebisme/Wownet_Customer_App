package com.example.wowrackcustomerapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.databinding.ActivityMainBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.auth.login.LoginActivity
import com.example.wowrackcustomerapp.ui.main.section.help.HelpActivity
import com.example.wowrackcustomerapp.ui.main.section.hotspot.HotspotFragment
import com.example.wowrackcustomerapp.ui.main.section.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeButtonEnabled(true)
//
//        // Enable options menu in the fragment
//        val fragment = com.example.wowrackcustomerapp.ui.main.section.hotspot.HotspotFragment()
//        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
//
//        fragment.setHasOptionsMenu(true)
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)


        viewModel.getSession().observe(this){user->
            if (!user.isLogin){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            userId = user.userId
        }
        setupView()
    }
    private fun setupView(){
//        loadFragment(HomeFragment())
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
//                R.id.home -> {
//                    loadFragment(HomeFragment())
//                    true
//                }
                R.id.hotspot -> {
                    loadFragment(HotspotFragment())
//                    val intent = Intent(this,HotspotActivity::class.java)
//                    startActivity(intent)
                    true
                }
                R.id.help -> {
//                    loadFragment(HelpFragment())
                    val intent = Intent(this,HelpActivity::class.java)
                    intent.putExtra("senderId",userId)
                    Log.d("userIdHome",userId)
                    startActivity(intent)
                    false
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