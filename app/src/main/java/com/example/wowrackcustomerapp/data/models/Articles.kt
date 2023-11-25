package com.example.wowrackcustomerapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Articles (
    val name: String,
    val description: String,
    val photo: Int
): Parcelable