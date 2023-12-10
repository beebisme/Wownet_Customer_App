package com.example.wowrackcustomerapp.ui.main.section.hotspot

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.adapter.HotspotAdapter
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.response.DataHotspot
import com.example.wowrackcustomerapp.data.response.HotspotResponse
import com.example.wowrackcustomerapp.databinding.ActivityWownetHotspotBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class WownetHotspot : AppCompatActivity() {

    private lateinit var binding: ActivityWownetHotspotBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var hotspotAdapter: HotspotAdapter
    private val viewModel by viewModels<HotspotViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var geocoder: Geocoder
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private lateinit var distanceText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWownetHotspotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        geocoder = Geocoder(this, Locale.getDefault())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        recyclerView = binding.recyclerViewHotspot
        hotspotAdapter = HotspotAdapter()
        recyclerView.adapter = hotspotAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        if (checkLocationPermission()) {
            loadHotspotDataWithLocWithDistance()
        } else {
            requestLocationPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkLocationPermission()) {
            loadHotspotDataWithLocWithDistance()
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

    private fun loadHotspotDataWithLoc() {
        viewModel.getSession().observe(this@WownetHotspot) { session ->
            if (session != null) {
                if (checkLocationPermission()) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            val client = ApiConfig.getService(session.token).getHotspot()
                            client.enqueue(object : Callback<HotspotResponse> {
                                override fun onResponse(
                                    call: Call<HotspotResponse>,
                                    response: Response<HotspotResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val responseBody = response.body()
                                        responseBody?.let {
                                            val userLocation = Location("UserLocation").apply {
                                                latitude = location.latitude
                                                longitude = location.longitude
                                            }

                                            val radiusInMeters = 10000.0
                                            val hotspotsWithinRadius = it.data.filter { hotspot ->
                                                val hotspotLocation = hotspot.getLocation()
                                                hotspot.calculateDistance(
                                                    userLocation,
                                                    hotspotLocation
                                                ) <= radiusInMeters

                                            }


                                            updateHotspotList(hotspotsWithinRadius)

                                            Log.d("lang", location.latitude.toString())
                                            Log.d("long", location.longitude.toString())

                                            displayUserAddress(location.latitude, location.longitude)
                                        }
                                    } else {
                                        Log.e(
                                            "ApiError",
                                            "Failed to fetch hotspot details. Code: ${response.code()}"
                                        )
                                    }
                                }

                                override fun onFailure(call: Call<HotspotResponse>, t: Throwable) {
                                    Log.e("ApiError", "Failed to fetch hotspot details.", t)
                                }
                            })
                        }
                    }
                } else {
                    Log.e("WownetHotspot", "Location permission not granted")
                }
            } else {
                Log.e("WownetHotspot", "Session is null")
            }
        }
    }

    private fun loadHotspotDataWithLocWithDistance() {
        viewModel.getSession().observe(this@WownetHotspot) { session ->
            if (session != null) {
                if (checkLocationPermission()) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            val client = ApiConfig.getService(session.token).getHotspot()
                            client.enqueue(object : Callback<HotspotResponse> {
                                override fun onResponse(
                                    call: Call<HotspotResponse>,
                                    response: Response<HotspotResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val responseBody = response.body()
                                        responseBody?.let {
                                            val userLocation = Location("UserLocation").apply {
                                                latitude = location.latitude
                                                longitude = location.longitude
                                            }

                                            // Perbarui jarak untuk setiap hotspot
                                            it.data.forEach { hotspot ->
                                                hotspot.updateDistance(userLocation)
                                            }

                                            val radiusInMeters = 1000.0
                                            val filteredHotspots = it.data.filter { hotspot ->
                                                hotspot.distance <= radiusInMeters
                                            }

                                            // Ubah nilai distanceText berdasarkan jarak hotspot pertama
                                            distanceText = if (filteredHotspots.isNotEmpty()) {
                                                val distance = filteredHotspots.first().distance
                                                if (distance < radiusInMeters) {
                                                    String.format("%.0f meters", distance)
                                                } else {
                                                    String.format("%.2f km", distance / 1000)
                                                }
                                            } else {
                                                "No hotspots within the radius"
                                            }

                                            // Tampilkan hotspot yang berada dalam radius
                                            updateHotspotList(filteredHotspots)

                                            // Tampilkan alamat user pada tv_hotspot
                                            displayUserAddress(location.latitude, location.longitude)
                                        }
                                    } else {
                                        Log.e(
                                            "ApiError",
                                            "Failed to fetch hotspot details. Code: ${response.code()}"
                                        )
                                    }
                                }

                                override fun onFailure(call: Call<HotspotResponse>, t: Throwable) {
                                    Log.e("ApiError", "Failed to fetch hotspot details.", t)
                                }
                            })
                        }
                    }
                } else {
                    Log.e("WownetHotspot", "Location permission not granted")
                }
            } else {
                Log.e("WownetHotspot", "Session is null")
            }
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadHotspotDataWithLocWithDistance()
            } else {
                Log.e("WownetHotspot", "Location permission denied")
            }
        }
    }

    private fun displayUserAddress(latitude: Double, longitude: Double) {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val userAddress = addresses[0].getAddressLine(0)
                binding.tvHotspot.text = "Hotspot - $userAddress"
            } else {
                Log.e("WownetHotspot", "No address found for the given coordinates")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
