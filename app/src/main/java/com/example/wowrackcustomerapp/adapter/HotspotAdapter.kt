package com.example.wowrackcustomerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.data.models.Hotspot
import com.example.wowrackcustomerapp.databinding.ItemHotspotBinding

class HotspotAdapter(private val listHotspot: List<Hotspot>) : RecyclerView.Adapter<HotspotAdapter.ListViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val binding: ItemHotspotBinding = ItemHotspotBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ListViewHolder(binding)
        }

        override fun getItemCount(): Int = listHotspot.size

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            val hotspot = listHotspot[position]
            holder.bind(hotspot)
        }

        class ListViewHolder(private val binding: ItemHotspotBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(hotspot: Hotspot) {
                binding.apply {
                    tvHotspotName.text = hotspot.hotspot

                    // Anda dapat menambahkan listener klik di sini
                    root.setOnClickListener {
                        // Handle klik di sini
                    }
                }
            }
        }
    }
