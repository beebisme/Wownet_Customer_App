package com.example.wowrackcustomerapp.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServicePackages(
    val serviceName: String,
    val serviceSpecs: String,
    val serviceDate: String,
    val serviceLocation: String,
):Parcelable
