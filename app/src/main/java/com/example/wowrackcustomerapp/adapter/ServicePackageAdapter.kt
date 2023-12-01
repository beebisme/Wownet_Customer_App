package com.example.wowrackcustomerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.data.models.ServicePackages
import com.example.wowrackcustomerapp.databinding.ItemPackageBinding
import com.example.wowrackcustomerapp.ui.main.section.modifypackage.ModifyPackageActivity

class ServicePackageAdapter(private val listService: List<ServicePackages>): RecyclerView.Adapter<ServicePackageAdapter.ListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding: ItemPackageBinding = ItemPackageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listService.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val service = listService[position]
        holder.bind(service)
    }


    class ListViewHolder(private val binding: ItemPackageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: ServicePackages) {
            binding.apply {
                tvPackageName.text = service.serviceName
                tvPackageSpecs.text = service.serviceSpecs
                tvPackageDate.text = service.serviceDate
                tvPackageLocation.text = service.serviceLocation

                editIcon.setOnClickListener {
                    val intent = Intent(itemView.context, ModifyPackageActivity::class.java)
                    itemView.context.startActivity(intent)
                }
            }
        }


    }
}