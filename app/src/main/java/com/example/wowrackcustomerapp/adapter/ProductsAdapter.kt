package com.example.wowrackcustomerapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.data.response.ServiceItem
import com.example.wowrackcustomerapp.databinding.ItemProductsBinding

class ProductsAdapter(private val listProduct: List<ServiceItem>): RecyclerView.Adapter<ProductsAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding: ItemProductsBinding = ItemProductsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val product = listProduct[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = listProduct.size

    class ListViewHolder(private val binding: ItemProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ServiceItem) {
            binding.apply {
                tvPackage.text = product.name
                tvSpeed.text = product.speed
                tvPrice.text = product.price

                // Anda dapat menambahkan listener klik di sini
                root.setOnClickListener {
                    // Handle klik di sini
                }
            }
        }
    }
}