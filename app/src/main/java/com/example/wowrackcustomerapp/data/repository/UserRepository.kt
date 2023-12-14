package com.example.wowrackcustomerapp.data.repository

import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.api.ApiService
import com.example.wowrackcustomerapp.data.models.UserModel
import com.example.wowrackcustomerapp.data.pref.UserPreference
import com.example.wowrackcustomerapp.data.response.LoginApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
){
    suspend fun login(email: String, password: String): Call<LoginApiResponse> {
        // Use ApiService to perform the login request
        return apiService.login(email, password)
    }
    suspend fun saveSession(userModel: UserModel){
        userPreference.saveSession(userModel)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout(){
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference,apiService)
            }.also { instance = it }
    }
}