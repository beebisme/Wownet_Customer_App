package com.example.wowrackcustomerapp.data.injection

import android.content.Context
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.pref.UserPreference
import com.example.wowrackcustomerapp.data.pref.dataStore
import com.example.wowrackcustomerapp.data.repository.ArticleRepository
import com.example.wowrackcustomerapp.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideArticleRepository(context: Context): ArticleRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking {
            pref.getSession().first()
        }
        val apiService = ApiConfig.getService(user.token)
        return ArticleRepository(apiService)
    }

}