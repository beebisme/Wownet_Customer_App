package com.example.wowrackcustomerapp.ui.main.section.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.models.Articles
import com.example.wowrackcustomerapp.data.repository.ArticleRepository
import com.example.wowrackcustomerapp.data.response.DataItem
import com.example.wowrackcustomerapp.databinding.ActivityDetailArticleBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.main.section.home.HomeActivity

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
            viewModel.getArticleById(articleId)
            viewModel.article.observe(this, { article ->
                updateUI(article)
            })
        }
    }

    private fun updateUI(article: DataItem) {
        val tvDetailName: TextView = binding.tvTitle
        val tvDetailDescription: TextView = binding.tvDescription
        val ivDetailPhoto: ImageView = binding.ivArticle

        tvDetailName.text = article.title
        tvDetailDescription.text = article.description

        // Load image using Glide or your preferred image loading library
        Glide.with(this)
            .load(article.imageUrl)
            .into(ivDetailPhoto)
    }

    companion object {
        const val EXTRA_DETAIL_ID = "extra_detail_id"
    }
}