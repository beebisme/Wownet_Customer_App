package com.example.wowrackcustomerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.data.models.ServicePackages
import com.example.wowrackcustomerapp.databinding.ItemModifyPackageBinding
import com.example.wowrackcustomerapp.ui.main.section.modifypackage.ConfirmModifyActivity

class ModifyPackageAdapter(private val listService: List<ServicePackages>): RecyclerView.Adapter<ModifyPackageAdapter.ListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding: ItemModifyPackageBinding = ItemModifyPackageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listService.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val service = listService[position]
        holder.bind(service)
    }


    class ListViewHolder(private val binding: ItemModifyPackageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: ServicePackages) {
            binding.apply {
                tvPackage.text = service.serviceName
                tvSpeed.text = service.serviceSpecs

                IvGoConfirm.setOnClickListener {
                    val intent = Intent(itemView.context, ConfirmModifyActivity::class.java)
                    itemView.context.startActivity(intent)
                }
            }
        }


    }
}