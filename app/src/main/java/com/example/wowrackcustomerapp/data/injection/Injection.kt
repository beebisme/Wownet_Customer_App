package com.example.wowrackcustomerapp.data.injection

import android.content.Context
import com.example.wowrackcustomerapp.data.pref.UserPreference
import com.example.wowrackcustomerapp.data.pref.dataStore
import com.example.wowrackcustomerapp.data.repository.UserRepository

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}