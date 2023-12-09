package com.example.wowrackcustomerapp.data.repository

import com.example.wowrackcustomerapp.data.api.ApiService
import com.example.wowrackcustomerapp.data.response.DataArticle
import com.example.wowrackcustomerapp.data.response.DataItem
import retrofit2.Response

class ArticleRepository(private val apiService: ApiService) {

    suspend fun getArticleById(articleId: Int): Response<DataArticle> {
        return apiService.getArticleById(articleId)
    }

}