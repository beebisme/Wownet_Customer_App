package com.example.wowrackcustomerapp.data.api

import com.example.wowrackcustomerapp.data.response.LoginApiResponse
import com.example.wowrackcustomerapp.data.response.LoginOTPResponse
import com.example.wowrackcustomerapp.data.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginApiResponse>

    @FormUrlEncoded
    @POST("users/otp")
    fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): Call<LoginOTPResponse>
}