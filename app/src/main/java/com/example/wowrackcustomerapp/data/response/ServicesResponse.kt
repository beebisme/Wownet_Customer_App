package com.example.wowrackcustomerapp.data.response

import com.google.gson.annotations.SerializedName

data class ServicesResponse(

	@field:SerializedName("data")
	val data: List<ServiceItem>,

	@field:SerializedName("article_count")
	val articleCount: Int
)

data class ServiceItem(

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("speed")
	val speed: String
)
