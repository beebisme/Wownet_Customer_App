package com.example.wowrackcustomerapp.ui.auth.login

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.models.UserModel
import com.example.wowrackcustomerapp.data.response.LoginApiResponse
import com.example.wowrackcustomerapp.databinding.ActivityLoginBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.auth.otp.OneTimePassActivity
import com.example.wowrackcustomerapp.ui.main.section.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                binding = ActivityLoginBinding.inflate(layoutInflater)
                setContentView(binding.root)

                setupView()
                setupAction()
            }
        }

    }

    private fun setupView() {
        progressBar = binding.progressBarLogin
        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        val progressBar = binding.progressBarLogin
        binding.buttonLogin.setOnClickListener {
            binding.buttonLogin.isEnabled = false
            progressBar.visibility = View.VISIBLE
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val client = ApiConfig.getService("").login(email, password)
            client.enqueue(object : Callback<LoginApiResponse> {
                override fun onResponse(
                    call: Call<LoginApiResponse>,
                    response: Response<LoginApiResponse>
                ) {

                    progressBar.visibility = View.GONE
                    val responseBody = response.body()
                    if (responseBody != null) {
                        viewModel.saveSession(
                            UserModel(
                                responseBody.data.id.toString(),
                                responseBody.data.name,
                                responseBody.data.email,
                                responseBody.data.token,
                                false
                            )
                        )
                        ViewModelFactory.clearInstance()
                        Toast.makeText(
                            this@LoginActivity,
                            "Success",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this@LoginActivity, OneTimePassActivity::class.java)
                        intent.putExtra("token", responseBody.data.token)
                        intent.putExtra("email", responseBody.data.email)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }else{
                        binding.buttonLogin.isEnabled = true
                    }
                }

                override fun onFailure(call: Call<LoginApiResponse>, t: Throwable) {
                    binding.buttonLogin.isEnabled = true
                    progressBar.visibility = View.GONE
                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                    AlertDialog.Builder(this@LoginActivity).apply {
                        setTitle("Oops!")
                        setMessage("${t.message}")
                        setPositiveButton("OK") { _, _ -> }
                        create()
                        show()
                    }
                }

            })
        }
    }
}