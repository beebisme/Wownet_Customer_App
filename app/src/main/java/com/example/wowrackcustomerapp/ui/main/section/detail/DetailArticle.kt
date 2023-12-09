package com.example.wowrackcustomerapp.ui.main.section.detail

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.response.DataArticle
import com.example.wowrackcustomerapp.data.response.DetailArticleResponse
import com.example.wowrackcustomerapp.databinding.ActivityDetailArticleBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailArticle : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding
    private val viewModel by viewModels<DetailArticleViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val articleId = intent.getIntExtra(EXTRA_DETAIL_ID, -1)

        if (articleId != -1) {
//            viewModel.getArticleById(articleId)
//            viewModel.article.observe(this) { article ->
//                updateUI(article)
//            }
            updateUI(articleId)
        }
    }

    private fun updateUI(articleId: Int) {
//        article?.let {
//            val tvDetailName: TextView = binding.tvTitle
//            val tvDetailDescription: TextView = binding.tvDescription
//            val ivDetailPhoto: ImageView = binding.ivArticle
//
//            tvDetailName.text = it.title
//            tvDetailDescription.text = it.description
//            Log.d("article",it.toString())
//
////            Log.d("arttitle", it.title)
////            Log.d("artdesc", it.description)
////            Log.d("artimg", it.imageUrl)
//
//            // Load image using Glide or your preferred image loading library
//            Glide.with(this)
//                .load(it.imageUrl)
//                .into(ivDetailPhoto)
//        } ?: run {
//            // Handle the case when article is null
//            Log.e("DetailArticle", "Article is null")
//        }
        viewModel.getSession().observe(this@DetailArticle) { session ->
            if (session != null) {
                val client = ApiConfig.getService(session.token).getArticleById(articleId)
                client.enqueue(object : Callback<DetailArticleResponse> {
                    override fun onResponse(call: Call<DetailArticleResponse>, response: Response<DetailArticleResponse>) {
                        val tvDetailName: TextView = binding.tvTitle
                        val tvDetailDescription: TextView = binding.tvDescription
                        val ivDetailPhoto: ImageView = binding.ivArticle

                        if (response.isSuccessful && response.body() != null) {
                            val article = response.body()!!
                            tvDetailName.text = article.data.title
                            tvDetailDescription.text = article.data.description
                            Glide.with(this@DetailArticle)
                                .load(article.data.imageUrl)
                                .into(ivDetailPhoto)
//                            Log.d("article", article.toString())
//
//                            Log.d("arttitle", article.title)
//                            Log.d("artdesc", article.description)
//                            Log.d("artimg", article.imageUrl)
                        } else {
                            Log.e("ApiError", "Failed to fetch article details. Code: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<DetailArticleResponse>, t: Throwable) {
                        Log.e("ApiError", "Failed to fetch article details.", t)
                    }
                })
            } else {
                Log.e("DetailArticle", "Session is null")
            }
        }

    }

    companion object {
        const val EXTRA_DETAIL_ID = "extra_detail_id"
    }
}
