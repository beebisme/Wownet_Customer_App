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
import androidx.core.widget.addTextChangedListener
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.models.UserModel
import com.example.wowrackcustomerapp.data.response.LoginApiResponse
import com.example.wowrackcustomerapp.data.response.LoginResult
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
    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                if (!user.isBiometric) {
                    binding.biometricLoginButton.visibility = View.GONE
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    setContentView(binding.root)
                    binding.biometricLoginButton.visibility = View.VISIBLE
                    setupView()
                    setupAction()
                }
            } else {
                setContentView(binding.root)
                binding.biometricLoginButton.visibility = View.GONE
                setupView()
                setupAction()
            }
        }
        viewModel.loginResult.observe(this) { result ->
            handleLoginResult()
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
        binding.buttonLogin.isEnabled = false
        binding.emailEditText.addTextChangedListener {
            checkLoginButtonStatus()
        }

        binding.passwordEditText.addTextChangedListener {
            checkLoginButtonStatus()
        }

        binding.buttonLogin.setOnClickListener {
            email = binding.emailEditText.text.toString()
            password = binding.passwordEditText.text.toString()
            viewModel.login(email, password)
        }
        binding.biometricLoginButton.setOnClickListener {
            showBiometricPrompt()
        }
    }

    private fun handleLoginResult() {
        viewModel.loginResult.observe(this) {
            if (it != null) {
                if (it.data != null) {
                    progressBar.visibility = View.GONE
                    viewModel.saveSession(
                        UserModel(
                            it.data.id.toString(),
                            it.data.name,
                            it.data.email,
                            password,
                            it.data.token,
                            it.data.phone,
                            it.data.address,
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
                    intent.putExtra("token", it.data.token)
                    intent.putExtra("email", it.data.email)
                    intent.putExtra("password", password)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }


                if (it.error != null) {
                    binding.buttonLogin.isEnabled = true
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginActivity,
//                                "Incorrect email or password",
                            it.error!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            }
        }
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential").setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(this,
            ContextCompat.getMainExecutor(this),
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
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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

    private fun checkLoginButtonStatus() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid =
            password.isNotEmpty() && password.length >= 8 // Add your password validation logic here

        binding.buttonLogin.isEnabled = isEmailValid && isPasswordValid
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}