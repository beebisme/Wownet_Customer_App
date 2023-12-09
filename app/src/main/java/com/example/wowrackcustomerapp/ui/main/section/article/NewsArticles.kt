package com.example.wowrackcustomerapp.ui.main.section.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.adapter.ArticleAdapter
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.models.Articles
import com.example.wowrackcustomerapp.data.response.ArticleResponse
import com.example.wowrackcustomerapp.databinding.ActivityNewsArticlesBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.main.section.home.HomeViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsArticles : AppCompatActivity() {

    private lateinit var binding: ActivityNewsArticlesBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel by viewModels<NewsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerViewArticles
        viewModel.getSession().observe(this) {
            getArticles(it.token)
        }

//        val articleList = getListHeroes()
//
//        // Set up RecyclerView with the list of articles
//        val articleAdapter = ArticleAdapter(articleList)
//        recyclerView.adapter = articleAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getArticles(token: String) {
        val apiService = ApiConfig.getService(token)
        apiService.getArticles().enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                if (response.isSuccessful) {
                    val articles = response.body()?.data ?: emptyList()
                    val articleAdapter = ArticleAdapter(articles)
                    Log.d("article", articles.toString())
                    Log.d("tokenapi", token)
                    recyclerView.adapter = articleAdapter
                } else {
                    // Handle error
                    Log.e("ApiError", "Failed to fetch articles. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                // Handle failure
                Log.e("ApiError", "Failed to fetch articles. Error: ${t.message}")
            }
        })
    }
}