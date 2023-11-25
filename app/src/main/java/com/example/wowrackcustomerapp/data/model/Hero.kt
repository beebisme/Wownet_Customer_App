package com.example.wowrackcustomerapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hero (
    val name: String,
    val description: String,
    val photo: Int
): Parcelable