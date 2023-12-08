package com.example.wowrackcustomerapp.ui.auth.otp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.models.UserModel
import com.example.wowrackcustomerapp.data.response.LoginApiResponse
import com.example.wowrackcustomerapp.data.response.LoginOTPResponse
import com.example.wowrackcustomerapp.databinding.ActivityOneTimePassBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.auth.login.LoginActivity
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
    private var password: String = ""
    private var token: String = ""
    private var countdown_timer: CountDownTimer? = null
    private var time_in_milliseconds = 60000L
    private val time_in_minutes = 1L
    private var pauseOffSet = 0L
    private var submitAttempts = 0
    private var resendAttempts = 0
    private val maxAttempts = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOneTimePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra("token").toString()
        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()
        Log.d("tokenout", token)
        Log.d("emailout", email)
        setupAction()
    }

    private fun setupAction() {
        starTimer(pauseOffSet)
        binding.clickmeTv.setTextColor(getColor(R.color.grey))
        resendOTP()
        submitOTP()
    }

    private fun starTimer(pauseOffSetL: Long) {
        countdown_timer =
            object : CountDownTimer(time_in_minutes * 60 * 1000 - pauseOffSetL, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    pauseOffSet = time_in_minutes * 60 * 1000 - millisUntilFinished
                    val minutes = (millisUntilFinished / 1000) / 60
                    val seconds = (millisUntilFinished / 1000) % 60
                    binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
                    binding.tvTimer.visibility = View.VISIBLE
                }

                override fun onFinish() {
                    resetTimer()
                    binding.tvTimer.visibility = View.GONE
                    binding.clickmeTv.setTextColor(getColor(R.color.orange))
                }
            }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun resetTimer() {
        if (countdown_timer != null) {
            countdown_timer!!.cancel()
            binding.tvTimer.text = " ${(time_in_milliseconds / 1000).toString()}"
            countdown_timer = null
            pauseOffSet = 0
        }
    }

    private fun resendOTP() {
        binding.clickmeTv.setOnClickListener {
            if (resendAttempts < maxAttempts) {
                binding.progressBarOTP.visibility = View.VISIBLE
                binding.clickmeTv.setTextColor(getColor(R.color.grey))
                resendAttempts++
                checkAttemptsAndRedirect()

                val client = ApiConfig.getService("").login(email, password)
                client.enqueue(object : Callback<LoginApiResponse> {
                    override fun onResponse(
                        call: Call<LoginApiResponse>,
                        response: Response<LoginApiResponse>
                    ) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            viewModel.saveSession(
                                UserModel(
                                    responseBody.data.id.toString(),
                                    responseBody.data.name,
                                    responseBody.data.email,
                                    password,
                                    responseBody.data.token,
                                    false
                                )
                            )
                            token = responseBody.data.token
                            ViewModelFactory.clearInstance()
                            Toast.makeText(
                                this@OneTimePassActivity,
                                "OTP successfully resent",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        binding.progressBarOTP.visibility = View.GONE
                        starTimer(pauseOffSet)
//                        resendAttempts = 0 // Reset resend attempts on success
                    }

                    override fun onFailure(call: Call<LoginApiResponse>, t: Throwable) {
                        binding.progressBarOTP.visibility = View.GONE
                        Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                        AlertDialog.Builder(this@OneTimePassActivity).apply {
                            setTitle("Oops!")
                            setMessage("${t.message}")
                            setPositiveButton("OK") { _, _ -> }
                            create()
                            show()
                        }
//                        resendAttempts++
                        checkAttemptsAndRedirect()
                    }
                })
            } else {
                Toast.makeText(
                    this@OneTimePassActivity,
                    "Maximum resend attempts reached. Please login again.",
                    Toast.LENGTH_LONG
                ).show()
                redirectToLogin()
            }
        }
    }

    private fun submitOTP() {
        binding.buttonSubmit.setOnClickListener {
            if (submitAttempts < maxAttempts) {
                binding.progressBarOTP.visibility = View.VISIBLE
                val otp = binding.OTPEditText.text.toString()
                val client = ApiConfig.getService(token).verifyOtp(email, otp)
                client.enqueue(object : Callback<LoginOTPResponse> {
                    override fun onResponse(
                        call: Call<LoginOTPResponse>,
                        response: Response<LoginOTPResponse>
                    ) {
                        binding.progressBarOTP.visibility = View.GONE
                        val responseBody = response.body()
                        if (response.isSuccessful && responseBody != null) {
                            if (responseBody.success != null) {
                                Toast.makeText(
                                    this@OneTimePassActivity,
                                    "Success",
                                    Toast.LENGTH_LONG
                                ).show()
                                viewModel.getSession().observe(this@OneTimePassActivity) {
                                    viewModel.saveSession(
                                        UserModel(
                                            it.userId,
                                            it.name,
                                            it.email,
                                            it.password,
                                            it.token,
                                            true
                                        )
                                    )
                                }
                                submitAttempts++
                                checkAttemptsAndRedirect()
                                val intent =
                                    Intent(this@OneTimePassActivity, HomeActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                binding.progressBarOTP.visibility = View.GONE
                                val errorMessage = responseBody.error.message
                                Toast.makeText(
                                    this@OneTimePassActivity,
                                    errorMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            submitAttempts++
                            checkAttemptsAndRedirect()
                        } else {
                            binding.progressBarOTP.visibility = View.GONE
                            Toast.makeText(
                                this@OneTimePassActivity,
                                "An error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                            submitAttempts++
                            checkAttemptsAndRedirect()
                        }
                    }

                    override fun onFailure(call: Call<LoginOTPResponse>, t: Throwable) {
                        binding.progressBarOTP.visibility = View.GONE
                        Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                        AlertDialog.Builder(this@OneTimePassActivity).apply {
                            setTitle("Oops!")
                            setMessage("${t.message}")
                            setPositiveButton("OK") { _, _ -> }
                            create()
                            show()
                        }
                        submitAttempts++
                        checkAttemptsAndRedirect()
                    }
                })
            } else {
                Toast.makeText(
                    this@OneTimePassActivity,
                    "Maximum submit attempts reached. Please login again.",
                    Toast.LENGTH_LONG
                ).show()
                redirectToLogin()
            }
        }
    }

    private fun checkAttemptsAndRedirect() {
        if (submitAttempts >= maxAttempts || resendAttempts >= maxAttempts) {
            Log.d("submit", submitAttempts.toString())
            redirectToLogin()
        }
    }

    private fun redirectToLogin() {
        // Add code to navigate back to the login page
        val intent = Intent(this@OneTimePassActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

}