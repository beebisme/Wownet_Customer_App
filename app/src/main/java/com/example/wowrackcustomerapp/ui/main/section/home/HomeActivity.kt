package com.example.wowrackcustomerapp.ui.main.section.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.adapter.ArticleAdapter
import com.example.wowrackcustomerapp.adapter.HotspotAdapter
import com.example.wowrackcustomerapp.data.models.Articles
import com.example.wowrackcustomerapp.databinding.ActivityHomeBinding
import com.example.wowrackcustomerapp.ui.main.section.article.NewsArticles
import com.example.wowrackcustomerapp.ui.main.section.detail.DetailTraffic
import com.example.wowrackcustomerapp.ui.main.section.hotspot.WownetHotspot
import com.example.wowrackcustomerapp.ui.main.section.help.HelpActivity
import com.example.wowrackcustomerapp.ui.main.section.profile.ProfileActivity
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
            val intent = Intent(this,HelpActivity::class.java)
            intent.putExtra("senderId","1")
            Log.d("userIdHome","1")
            startActivity(intent)
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

            profilePhoto.setOnClickListener {
                startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
            }

            seeAll.setOnClickListener {
                startActivity(Intent(this@HomeActivity, NewsArticles::class.java))
            }

            clShape.setOnClickListener{
                startActivity(Intent(this@HomeActivity, WownetHotspot::class.java))
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
