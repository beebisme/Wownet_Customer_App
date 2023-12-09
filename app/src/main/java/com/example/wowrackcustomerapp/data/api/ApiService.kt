package com.example.wowrackcustomerapp.data.api

import com.example.wowrackcustomerapp.data.response.ArticleResponse
import com.example.wowrackcustomerapp.data.response.DataArticle
import com.example.wowrackcustomerapp.data.response.DataItem
import com.example.wowrackcustomerapp.data.response.DetailArticleResponse
import com.example.wowrackcustomerapp.data.response.LoginApiResponse
import com.example.wowrackcustomerapp.data.response.LoginOTPResponse
import com.example.wowrackcustomerapp.data.response.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("articles")
    fun getArticles(): Call<ArticleResponse>

    @GET("articles/{id}")
    fun getArticleById(@Path("id") articleId: Int): Call<DetailArticleResponse>
}