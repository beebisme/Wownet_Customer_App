package com.example.wowrackcustomerapp.ui.main.section.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.adapter.InvoiceAdapter
import com.example.wowrackcustomerapp.adapter.ServicePackageAdapter
import com.example.wowrackcustomerapp.data.models.Invoice
import com.example.wowrackcustomerapp.data.models.ServicePackages
import com.example.wowrackcustomerapp.databinding.ActivityProfileBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var rvPackage: RecyclerView
    private lateinit var rvInvoice: RecyclerView
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

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

        binding.buttonLogout.setOnClickListener{
            viewModel.logout()
        }
    }

    private fun getListPackages() : List<ServicePackages> {
        val packageName = resources.getStringArray(R.array.service_name)
        val packageSpeed = resources.getStringArray(R.array.service_specs)
        val packageDate = resources.getStringArray(R.array.service_date)
        val packageLocation = resources.getStringArray(R.array.service_location)
        val listPackage = ArrayList<ServicePackages>()
        for (i in packageName.indices) {
            val article = ServicePackages(packageName[i], packageSpeed[i], packageDate[i], packageLocation[i] )
            listPackage.add(article)
        }
        return listPackage
    }

    private fun getListInvoice() : List<Invoice> {
        val invoiceName = resources.getStringArray(R.array.invoice_data)
        val listInvoice = ArrayList<Invoice>()
        for (i in invoiceName.indices) {
            val invoice = Invoice(invoiceName[i])
            listInvoice.add(invoice)
        }
        return listInvoice
    }


}