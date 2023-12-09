package com.example.wowrackcustomerapp.ui.main.section.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wowrackcustomerapp.data.repository.ArticleRepository
import com.example.wowrackcustomerapp.data.repository.UserRepository
import com.example.wowrackcustomerapp.data.response.DataArticle
import com.example.wowrackcustomerapp.data.response.DataItem
import kotlinx.coroutines.launch

class DetailArticleViewModel(private val repository: UserRepository,private val articleRepository: ArticleRepository) : ViewModel() {
    private val _article = MutableLiveData<DataArticle>()
    val article: LiveData<DataArticle> get() = _article

    fun getArticleById(articleId: Int) {
        viewModelScope.launch {
            try {
                val response = articleRepository.getArticleById(articleId)
                if (response.isSuccessful) {
                    _article.value = response.body()
                } else {
                    // Handle error
                    Log.e("ApiError", "Failed to fetch article details. Code: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle failure
                Log.e("ApiError", "Failed to fetch article details. Error: ${e.message}")
            }
        }
    }
}