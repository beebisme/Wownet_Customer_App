package com.example.wowrackcustomerapp.ui.main.section.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.data.models.Articles
import com.example.wowrackcustomerapp.databinding.ActivityDetailArticleBinding
import com.example.wowrackcustomerapp.ui.main.section.home.HomeActivity

class DetailArticle : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataHero = intent.getParcelableExtra<Articles>(EXTRA_DETAIL)

        val tvDetailName: TextView = binding.tvTitle
        val tvDetailDescription: TextView = binding.tvDescription
        val ivDetailPhoto = binding.ivArticle

        if (dataHero != null) {
            tvDetailName.text = dataHero.name
            tvDetailDescription.text = dataHero.description
            ivDetailPhoto.setImageResource(dataHero.photo)

        }
    }

    companion object{
        const val EXTRA_DETAIL = "extra_detail"
    }
}