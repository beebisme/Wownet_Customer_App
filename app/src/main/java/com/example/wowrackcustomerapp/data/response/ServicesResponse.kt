package com.example.wowrackcustomerapp.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ServicesResponse(
	val data: List<ServiceItem>,
	val articleCount: Int
) : Parcelable

@Parcelize
data class ServiceItem(
	val price: String,
	val name: String,
	val id: Int,
	val speed: String
) : Parcelable
