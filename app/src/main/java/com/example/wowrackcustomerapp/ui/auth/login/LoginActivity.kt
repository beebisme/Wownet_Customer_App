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
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
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
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin && !user.isBiometric) {
                binding.biometricLoginButton.visibility = View.GONE
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else if (user.isLogin && user.isBiometric) {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.biometricLoginButton.visibility = View.VISIBLE
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
                                password,
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
                        intent.putExtra("password", password)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    } else {
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
        binding.biometricLoginButton.setOnClickListener {
            showBiometricPrompt()
        }
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt =
            BiometricPrompt(this, ContextCompat.getMainExecutor(this),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        // Handle authentication error
                        showMessage("Authentication error: $errString")
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        // Handle authentication success
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                        showMessage("Authentication succeeded!")
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        // Handle authentication failure
                        showMessage("Authentication failed.")
                    }
                })

        biometricPrompt.authenticate(promptInfo)
    }

    private fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // The user can authenticate with biometrics, continue with the authentication process
                return true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE, BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE, BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Handle the error cases as needed in your app
                return false
            }

            else -> {
                // Biometric status unknown or another error occurred
                return false
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}