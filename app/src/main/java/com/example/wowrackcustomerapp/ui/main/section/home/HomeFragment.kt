package com.example.wowrackcustomerapp.ui.main.section.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.adapter.ArticleAdapter
import com.example.wowrackcustomerapp.data.models.Articles
import com.example.wowrackcustomerapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Inisialisasi RecyclerView dan Adapter
        recyclerView = binding.recyclerViewArticles
        adapter = ArticleAdapter(getListArticle())

        // Atur layout manager untuk RecyclerView (misalnya, LinearLayoutManager)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Set adapter untuk RecyclerView
        recyclerView.adapter = adapter

        return view
    }

    private fun getListArticle(): List<Articles> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)

        return dataName.indices.map { i ->
            Articles(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
        }
    }
}
