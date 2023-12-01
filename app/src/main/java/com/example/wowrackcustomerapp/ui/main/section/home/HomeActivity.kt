package com.example.wowrackcustomerapp.ui.main.section.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.adapter.ArticleAdapter
import com.example.wowrackcustomerapp.data.models.Articles
import com.example.wowrackcustomerapp.databinding.ActivityHomeBinding
import com.example.wowrackcustomerapp.ui.main.section.detail.DetailTraffic
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerViewArticles
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        floatingActionButton = binding.floatingActionButton
        floatingActionButton.setOnClickListener {
            // Handle FAB click here
        }

        // Get list of articles
        val articleList = getListHeroes()

        // Set up RecyclerView with the list of articles
        val articleAdapter = ArticleAdapter(articleList)
        recyclerView.adapter = articleAdapter

        binding.apply {
            clTrafficUsage.setOnClickListener {
                // Memulai DetailTrafficActivity
                startActivity(Intent(this@HomeActivity, DetailTraffic::class.java))
            }

            clTrafficUsage2.setOnClickListener {
                // Memulai DetailTrafficActivity
                startActivity(Intent(this@HomeActivity, DetailTraffic::class.java))
            }
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
}
