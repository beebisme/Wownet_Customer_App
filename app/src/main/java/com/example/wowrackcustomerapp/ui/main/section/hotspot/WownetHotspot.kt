package com.example.wowrackcustomerapp.ui.main.section.hotspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.adapter.HotspotAdapter
import com.example.wowrackcustomerapp.data.models.Hotspot
import com.example.wowrackcustomerapp.databinding.ActivityWownetHotspotBinding

class WownetHotspot : AppCompatActivity() {

    private lateinit var binding: ActivityWownetHotspotBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var hotspotAdapter: HotspotAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWownetHotspotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerViewHotspot
        // Set up RecyclerView with the list of articles
        hotspotAdapter = HotspotAdapter(getListHeroes())
        recyclerView.adapter = hotspotAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getListHeroes(): ArrayList<Hotspot> {
        val dataName = resources.getStringArray(R.array.item_hotspot_name)
        val listHotspot = ArrayList<Hotspot>()
        for (i in dataName.indices) {
            val hotspot = Hotspot(dataName[i])
            listHotspot.add(hotspot)
        }
        return listHotspot
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}