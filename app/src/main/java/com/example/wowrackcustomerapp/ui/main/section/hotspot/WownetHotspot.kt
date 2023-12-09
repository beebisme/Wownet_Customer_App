package com.example.wowrackcustomerapp.ui.main.section.hotspot

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.adapter.HotspotAdapter
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.models.Hotspot
import com.example.wowrackcustomerapp.data.response.DataHotspot
import com.example.wowrackcustomerapp.data.response.HotspotResponse
import com.example.wowrackcustomerapp.databinding.ActivityWownetHotspotBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.main.section.home.HomeViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WownetHotspot : AppCompatActivity() {

    private lateinit var binding: ActivityWownetHotspotBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var hotspotAdapter: HotspotAdapter
    private val viewModel by viewModels<HotspotViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWownetHotspotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerViewHotspot
        // Set up RecyclerView with the list of articles
        hotspotAdapter = HotspotAdapter()
        recyclerView.adapter = hotspotAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
        if (checkLocationPermission()) {
            loadHotspotData()
        } else {
            requestLocationPermission()
        }
    }

    private fun loadHotspotData() {
        viewModel.getSession().observe(this@WownetHotspot) { session ->
            if (session != null) {
                val client = ApiConfig.getService(session.token).getHotspot()
                client.enqueue(object : Callback<HotspotResponse> {
                    override fun onResponse(
                        call: Call<HotspotResponse>,
                        response: Response<HotspotResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                updateHotspotList(it.data)
                            }
                        } else {
                            Log.e("ApiError", "Failed to fetch hotspot details. Code: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<HotspotResponse>, t: Throwable) {
                        Log.e("ApiError", "Failed to fetch hotspot details.", t)
                    }
                })
            } else {
                Log.e("WownetHotspot", "Session is null")
            }
        }
    }

    private fun updateHotspotList(hotspots: List<DataHotspot>) {
        hotspotAdapter.submitList(hotspots)
    }
private fun checkLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

//    private fun loadHotspotData() {
//        viewModel.getSession().observe(this@WownetHotspot) { session ->
//            if (session != null) {
//                // Dapatkan lokasi pengguna
//                val locationManager =
//                    getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//                if (checkLocationPermission()) {
//                    val location =
//                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                    location?.let {
//                        // Dapatkan data hotspot
//                        val client = ApiConfig.getService(session.token).getHotspot()
//                        client.enqueue(object : Callback<HotspotResponse> {
//                            override fun onResponse(
//                                call: Call<HotspotResponse>,
//                                response: Response<HotspotResponse>
//                            ) {
//                                if (response.isSuccessful) {
//                                    val responseBody = response.body()
//                                    responseBody?.let {
//                                        // Hitung jarak dan urutkan hotspot
//                                        val userLocation = Location("UserLocation").apply {
//                                            latitude = location.latitude
//                                            longitude = location.longitude
//                                        }
//                                        val sortedHotspots = it.data.sortedBy {
//                                            calculateDistance(
//                                                userLocation,
//                                                it.getLocation()
//                                            )
//                                        }
//
//                                        Log.d("sorted",sortedHotspots.toString())
//                                        // Tampilkan hotspot terdekat
//                                        updateHotspotList(sortedHotspots)
//                                    }
//                                } else {
//                                    Log.e(
//                                        "ApiError",
//                                        "Failed to fetch hotspot details. Code: ${response.code()}"
//                                    )
//                                }
//                            }
//
//                            override fun onFailure(call: Call<HotspotResponse>, t: Throwable) {
//                                Log.e(
//                                    "ApiError",
//                                    "Failed to fetch hotspot details.",
//                                    t
//                                )
//                            }
//                        })
//                    }
//                } else {
//                    Log.e("WownetHotspot", "Location permission not granted")
//                }
//            } else {
//                Log.e("WownetHotspot", "Session is null")
//            }
//        }
//    }

//    private fun updateHotspotList(hotspots: List<DataHotspot>) {
//        hotspotAdapter.submitList(hotspots)
//    }

    private fun calculateDistance(userLocation: Location, hotspotLocation: Location): Float {
        return userLocation.distanceTo(hotspotLocation)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadHotspotData()
            } else {
                Log.e("WownetHotspot", "Location permission denied")
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}