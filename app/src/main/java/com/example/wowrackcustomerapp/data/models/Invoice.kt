package com.example.wowrackcustomerapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Invoice(
    val invoice: String,
): Parcelable
