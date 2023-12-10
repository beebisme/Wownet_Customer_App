package com.example.wowrackcustomerapp.ui.main.section.products

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.adapter.ProductsAdapter
import com.example.wowrackcustomerapp.data.api.ApiConfig
import com.example.wowrackcustomerapp.data.response.ServiceItem
import com.example.wowrackcustomerapp.data.response.ServicesResponse
import com.example.wowrackcustomerapp.databinding.ActivityProductsBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val viewModel by viewModels<ProductsViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var listProduct: ArrayList<ServiceItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.rvProducts
        viewModel.getSession().observe(this) {
            getProducts(it.token)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun getProducts(token: String) {
        listProduct = ArrayList()
        val apiService = ApiConfig.getService(token)
        apiService.getServices().enqueue(object : Callback<ServicesResponse> {
            override fun onResponse(
                call: Call<ServicesResponse>,
                response: Response<ServicesResponse>
            ) {
                if (response.isSuccessful) {
                    val products = response.body()?.data ?: emptyList()
                    //                        for (i in products){
//                            listProduct.add(i)
//                        }
//                        listProduct.addAll(products)
                    Log.d("products", products.toString())
                    val productAdapter = ProductsAdapter(products)
                    recyclerView.adapter = productAdapter
                }
            }

            override fun onFailure(call: Call<ServicesResponse>, t: Throwable) {
                Log.d("Service Data", "data : gagal")
            }
        })
    }
}