package com.example.wowrackcustomerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.data.models.Invoice
import com.example.wowrackcustomerapp.databinding.ItemInvoiceBinding

class InvoiceAdapter(private val listInvoice: List<Invoice>): RecyclerView.Adapter<InvoiceAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding: ItemInvoiceBinding = ItemInvoiceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val invoice = listInvoice[position]
        holder.bind(invoice)
    }

    override fun getItemCount(): Int = listInvoice.size

    class ListViewHolder(private val binding: ItemInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(invoice: Invoice) {
            binding.apply {
                tvInvoice.text = invoice.invoice

                // Anda dapat menambahkan listener klik di sini
                root.setOnClickListener {
                    // Handle klik di sini
                }
            }
        }
    }
}