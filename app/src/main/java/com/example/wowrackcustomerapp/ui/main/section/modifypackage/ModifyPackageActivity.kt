package com.example.wowrackcustomerapp.ui.main.section.modifypackage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.adapter.ModifyPackageAdapter
import com.example.wowrackcustomerapp.data.models.ServicePackages
import com.example.wowrackcustomerapp.databinding.ActivityModifyPackageBinding

class ModifyPackageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ModifyPackageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityModifyPackageBinding = ActivityModifyPackageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.rvPackage
        adapter = ModifyPackageAdapter(getListService())
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun getListService(): List<ServicePackages> {
        val serviceName = resources.getStringArray(R.array.item_package)
        val serviceSpecs = resources.getStringArray(R.array.item_specs)
        val serviceDate = resources.getStringArray(R.array.service_date)
        val serviceLocation = resources.getStringArray(R.array.service_location)

        return serviceName.indices.map { i ->
            ServicePackages(serviceName[i], serviceSpecs[i], serviceDate[i], serviceLocation[i])
        }
    }
}