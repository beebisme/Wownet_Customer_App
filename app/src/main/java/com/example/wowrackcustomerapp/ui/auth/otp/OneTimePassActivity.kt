package com.example.wowrackcustomerapp.ui.auth.otp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.response.LoginOTPResponse
import com.example.wowrackcustomerapp.databinding.ActivityOneTimePassBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.main.MainViewModel
import com.example.wowrackcustomerapp.ui.main.section.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OneTimePassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOneTimePassBinding
    private val viewModel by viewModels<OneTimePassViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var email: String = ""
    private var token: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOneTimePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel.getSession().observe(this) {
//            token = it.token
//            email = it.email
//        Log.d("token",token)
//        Log.d("email",email)
//        }
        token = intent.getStringExtra("token").toString()
        email = intent.getStringExtra("email").toString()
        Log.d("tokenout",token)
        Log.d("emailout",email)
        setupAction()
    }

    private fun setupAction() {
        binding.buttonSubmit.setOnClickListener {
            val otp = binding.OTPEditText.text.toString()
            val client = ApiConfig.getService(token).verifyOtp(email, otp)
            Log.d("tknon",token)
            client.enqueue(object : Callback<LoginOTPResponse> {
                override fun onResponse(
                    call: Call<LoginOTPResponse>,
                    response: Response<LoginOTPResponse>
                ) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.success != null) {
                            Toast.makeText(
                                this@OneTimePassActivity,
                                "Success",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(this@OneTimePassActivity, HomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@OneTimePassActivity,
                                responseBody.error.message,
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(this@OneTimePassActivity, OneTimePassActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
                }

                override fun onFailure(call: Call<LoginOTPResponse>, t: Throwable) {
//                    progressBar.visibility = View.GONE
                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                    AlertDialog.Builder(this@OneTimePassActivity).apply {
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