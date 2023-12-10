package com.example.wowrackcustomerapp.ui.main.section.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.adapter.ArticleAdapter
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.response.ArticleResponse
import com.example.wowrackcustomerapp.databinding.ActivityHomeBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.main.section.article.NewsArticles
import com.example.wowrackcustomerapp.ui.main.section.detail.DetailArticle
import com.example.wowrackcustomerapp.ui.main.section.detail.DetailTraffic
import com.example.wowrackcustomerapp.ui.main.section.hotspot.WownetHotspot
import com.example.wowrackcustomerapp.ui.main.section.help.HelpActivity
import com.example.wowrackcustomerapp.ui.main.section.profile.ProfileActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var token: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerViewArticles
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        floatingActionButton = binding.floatingActionButton
        floatingActionButton.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            intent.putExtra("senderId", "1")
            Log.d("userIdHome", "1")
            startActivity(intent)
        }
        viewModel.getSession().observe(this) {
            token = it.token
            Log.d("tokenhome", token)
            getArticles(it.token)
        }

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

            clShape.setOnClickListener {
                startActivity(Intent(this@HomeActivity, WownetHotspot::class.java))
            }

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSession().observe(this) {
            token = it.token
            Log.d("tokenhome", token)
            getArticles(it.token)
        }
    }

    //    private fun getListArticles(): List<Articles> {
//        val dataName = resources.getStringArray(R.array.data_name)
//        val dataDescription = resources.getStringArray(R.array.data_description)
//        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
//        val listArticle = ArrayList<Articles>()
//        for (i in dataName.indices) {
//            val article = Articles(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
//            listArticle.add(article)
//        }
//        dataPhoto.recycle() // Recycle the TypedArray to avoid memory leaks
//        return listArticle
////        val client = ApiConfig.getService()
//    }
    private fun getArticles(token: String) {
        val apiService = ApiConfig.getService(token)
        apiService.getArticles().enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                if (response.isSuccessful) {
                    val articles = response.body()?.data ?: emptyList()
                    val articleAdapter = ArticleAdapter(articles) { articleId ->
                        navigateToDetailActivity(articleId)
                    }
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
    private fun navigateToDetailActivity(articleId: Int) {
        val intent = Intent(this, DetailArticle::class.java)
        intent.putExtra(DetailArticle.EXTRA_DETAIL_ID, articleId)
        startActivity(intent)
    }
}
