package com.example.wowrackcustomerapp.ui.main.section.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.adapter.InvoiceAdapter
import com.example.wowrackcustomerapp.adapter.ServicePackageAdapter
import com.example.wowrackcustomerapp.data.models.Invoice
import com.example.wowrackcustomerapp.data.models.ServicePackages
import com.example.wowrackcustomerapp.data.models.UserModel
import com.example.wowrackcustomerapp.databinding.ActivityProfileBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.auth.login.LoginActivity
import com.example.wowrackcustomerapp.ui.main.section.home.HomeActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var rvPackage: RecyclerView
    private lateinit var rvInvoice: RecyclerView
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var isBiometricPromptShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvPackage = binding.rvPackage
        rvPackage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val packageAdapter = ServicePackageAdapter(getListPackages())
        rvPackage.adapter = packageAdapter

        rvInvoice = binding.rvInvoice
        rvInvoice.layoutManager = LinearLayoutManager(this)

        val invoiceAdapter = InvoiceAdapter(getListInvoice())
        rvInvoice.adapter = invoiceAdapter

        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }

        val switchBiometric = binding.biometricswitch
        viewModel.getSession().observe(this) {
            Log.d("biom", it.isBiometric.toString())
            switchBiometric.isChecked = it.isBiometric
            binding.tvName.text = it.name
            binding.tvEmail.text = it.email
            binding.tvPhoneNumber.text = it.phone

            switchBiometric.setOnCheckedChangeListener { buttonView, isChecked ->
                if (it.isBiometric != isChecked && isChecked){
                    viewModel.saveSession(
                        UserModel(
                            it.userId,
                            it.name,
                            it.email,
                            it.password,
                            it.token,
                            it.phone,
                            it.address,
                            it.isLogin,
                            true
                        )
                    )
                    Log.d("bio",it.isBiometric.toString())
                    switchBiometric.isChecked = isChecked

                }else{
                    viewModel.saveSession(
                        UserModel(
                            it.userId,
                            it.name,
                            it.email,
                            it.password,
                            it.token,
                            it.phone,
                            it.address,
                            it.isLogin,
                            false
                        )
                    )
                    switchBiometric.isChecked = isChecked
                }
            }
            binding.buttonLogout.setOnClickListener{
                viewModel.logout()
                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


//        switchBiometric.setOnCheckedChangeListener { _, isChecked ->
////            if (isChecked) {
//                // Switch is ON, show biometric authentication
//                showBiometricPrompt()
//                viewModel.getSession().observe(this@ProfileActivity){
//                    viewModel.saveSession(
//                        UserModel(
//                            it.userId,
//                            it.name,
//                            it.email,
//                            it.password,
//                            it.token,
//                            it.isLogin,
//                            isBiometricPromptShown
//                        )
//                    )
//                }
////            } else {
////                // Switch is OFF, set isBiometricPromptShown to false
////                isBiometricPromptShown = false
////            }
//        }

    }

    private fun getListPackages(): List<ServicePackages> {
        val packageName = resources.getStringArray(R.array.service_name)
        val packageSpeed = resources.getStringArray(R.array.service_specs)
        val packageDate = resources.getStringArray(R.array.service_date)
        val packageLocation = resources.getStringArray(R.array.service_location)
        val listPackage = ArrayList<ServicePackages>()
        for (i in packageName.indices) {
            val article =
                ServicePackages(packageName[i], packageSpeed[i], packageDate[i], packageLocation[i])
            listPackage.add(article)
        }
        return listPackage
    }

    private fun getListInvoice(): List<Invoice> {
        val invoiceName = resources.getStringArray(R.array.invoice_data)
        val listInvoice = ArrayList<Invoice>()
        for (i in invoiceName.indices) {
            val invoice = Invoice(invoiceName[i])
            listInvoice.add(invoice)
        }
        return listInvoice
    }



    private fun showBiometricPrompt() {
        // Check if biometric is supported before showing the prompt
        if (isBiometricSupported()) {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build()

            val biometricPrompt =
                BiometricPrompt(
                    this, ContextCompat.getMainExecutor(this),
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            // Handle authentication error
                            showMessage("Authentication error: $errString")
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            // Handle authentication success
                            showMessage("Authentication succeeded!")
                            // Set isBiometricPromptShown to true on successful authentication
                            viewModel.getSession().observe(this@ProfileActivity){
                            isBiometricPromptShown = !it.isBiometric
                            }
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            // Handle authentication failure
                            showMessage("Authentication failed.")
                        }
                    })

            biometricPrompt.authenticate(promptInfo)
        } else {
            // Biometric not supported, handle accordingly
            showMessage("Biometric authentication is not supported on this device.")
        }
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