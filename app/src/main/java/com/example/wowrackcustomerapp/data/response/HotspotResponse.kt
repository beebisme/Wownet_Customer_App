package com.example.wowrackcustomerapp.data.response

import android.location.Location
import com.google.gson.annotations.SerializedName

data class HotspotResponse(

	@field:SerializedName("data")
	val data: List<DataHotspot>,

	@field:SerializedName("hotspot_count")
	val hotspotCount: Int
)

data class DataHotspot(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("lang")
	val lang: Float,

	@field:SerializedName("long")
	val long: Float
) {
	fun getLocation(): Location {
		val location = Location("HotspotLocation")
		location.latitude = lang.toDouble()
		location.longitude = long.toDouble()
		return location
	}
}
