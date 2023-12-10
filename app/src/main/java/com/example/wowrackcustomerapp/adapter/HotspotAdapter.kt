package com.example.wowrackcustomerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.data.response.DataHotspot
import com.example.wowrackcustomerapp.databinding.ItemHotspotBinding

class HotspotAdapter :
    ListAdapter<DataHotspot, HotspotAdapter.ListViewHolder>(HotspotDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding: ItemHotspotBinding = ItemHotspotBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val hotspot = getItem(position)
        holder.bind(hotspot)
    }

    class ListViewHolder(private val binding: ItemHotspotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hotspot: DataHotspot) {
            binding.apply {
                tvHotspotName.text = hotspot.name
                val distanceText = if (hotspot.distance < 1000) {
                    String.format("%.0f meters", hotspot.distance)
                } else {
                    String.format("%.2f km", hotspot.distance / 1000)
                }
                tvHotspotDistance.text = distanceText
                // You can add a click listener here
                root.setOnClickListener {
                    // Handle the click here
                }
            }
        }
    }

    private class HotspotDiffCallback : DiffUtil.ItemCallback<DataHotspot>() {
        override fun areItemsTheSame(oldItem: DataHotspot, newItem: DataHotspot): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataHotspot, newItem: DataHotspot): Boolean {
            return oldItem == newItem
        }
    }
}
