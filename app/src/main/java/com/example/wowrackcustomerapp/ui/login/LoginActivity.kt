package com.example.wowrackcustomerapp.ui.login

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
import com.example.wowrackcustomerapp.data.response.LoginResponse
import com.example.wowrackcustomerapp.databinding.ActivityLoginBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.main.MainActivity
import com.example.wowrackcustomerapp.ui.main.section.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private lateinit var progressBar : ProgressBar
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSession().observe(this){user->
            if (user.isLogin){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }else{
                binding = ActivityLoginBinding.inflate(layoutInflater)
                setContentView(binding.root)

                setupView()
                setupAction()
            }
        }

    }

    private fun setupView() {
        progressBar = binding.progressBarLogin
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private fun setupAction(){
//        val progressBar = binding.progressBar
        binding.buttonLogin.setOnClickListener {
            binding.buttonLogin.isEnabled = false
            progressBar.visibility = View.VISIBLE
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val client = ApiConfig.getApiService("").login(email, password)
            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    progressBar.visibility = View.GONE
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (!responseBody.error) {
                            viewModel.saveSession(
                                UserModel(
                                    responseBody.loginResult.userId,
                                    responseBody.loginResult.name,
                                    responseBody.loginResult.token,
                                    true
                                )
                            )
                            ViewModelFactory.clearInstance()
                            Toast.makeText(
                                this@LoginActivity,
                                responseBody.message,
                                Toast.LENGTH_LONG
                            ).show()
//                            Toast(this@LoginActivity).showCustomToast("Selamat Anda Berhasil Login",this@LoginActivity, ColorDrawable(getColor(R.color.primary)))
//                            AlertDialog.Builder(this@LoginActivity).apply {
//                                setTitle("Yeah!")
//                                setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
//                                setPositiveButton("Lanjut") { _, _ ->
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
//                                }
//                                create()
//                                show()
//                            }
                        } else {
                            binding.buttonLogin.isEnabled = true
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@LoginActivity,
                                responseBody.message,
                                Toast.LENGTH_LONG
                            ).show()
//                            AlertDialog.Builder(this@LoginActivity).apply {
//                                setTitle("Ooops!")
//                                setMessage("Login failed")
//                                setPositiveButton("Lanjut") { _, _ ->
                            val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                                    startActivity(intent)
//                                    finish()
//                                }
//                                create()
//                                show()
//                            }
                        }
//                    }else{
//                        progressBar.visibility = View.GONE
//                        binding.buttonLogin.isEnabled = true
//                        Toast.makeText(this@LoginActivity,responseBody, Toast.LENGTH_LONG).show()
////                        AlertDialog.Builder(this@LoginActivity).apply {
////                            setTitle("Ooops!")
////                            setMessage("Login failed")
////                            setPositiveButton("Lanjut") { _, _ ->
//                                val intent = Intent(this@LoginActivity, LoginActivity::class.java)
//                                intent.flags =
//                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
////                                startActivity(intent)
////                                finish()
////                            }
////                            create()
////                            show()
////                        }
//                    }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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