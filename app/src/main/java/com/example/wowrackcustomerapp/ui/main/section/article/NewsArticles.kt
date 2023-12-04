package com.example.wowrackcustomerapp.ui.main.section.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.adapter.ArticleAdapter
import com.example.wowrackcustomerapp.adapter.HotspotAdapter
import com.example.wowrackcustomerapp.data.models.Articles
import com.example.wowrackcustomerapp.databinding.ActivityNewsArticlesBinding

class NewsArticles : AppCompatActivity() {

    private lateinit var binding: ActivityNewsArticlesBinding
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerViewArticles

        val articleList = getListHeroes()

        // Set up RecyclerView with the list of articles
        val articleAdapter = ArticleAdapter(articleList)
        recyclerView.adapter = articleAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getListHeroes(): List<Articles> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listArticle = ArrayList<Articles>()
        for (i in dataName.indices) {
            val article = Articles(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listArticle.add(article)
        }
        dataPhoto.recycle() // Recycle the TypedArray to avoid memory leaks
        return listArticle
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}