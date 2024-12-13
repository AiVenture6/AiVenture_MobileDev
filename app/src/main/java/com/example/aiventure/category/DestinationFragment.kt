package com.example.aiventure.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aiventure.R
import com.example.aiventure.category.adapter.CategoryAdapter
import com.example.aiventure.category.adapter.DestinationAdapter
import com.example.aiventure.category.datamodel.PlaceItemResponse
import com.example.aiventure.category.detail.DetailPlaceActivity
import com.example.aiventure.data.datastore.UserDatastore
import com.example.aiventure.data.service.ApiConfig
import com.example.aiventure.data.service.ApiService
import com.example.aiventure.recommendation.GridSpacingItemDecoration
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DestinationFragment : Fragment() {

    private var places: MutableList<PlaceItemResponse> = mutableListOf()
    private var categories: MutableList<CategoryItem> = mutableListOf()
    private lateinit var userDatastore: UserDatastore
    private lateinit var adapter: DestinationAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var rvPlace: RecyclerView
    private lateinit var rvCategories: RecyclerView
    private lateinit var loading: ProgressBar

    companion object {
        fun newInstance(city: String): DestinationFragment {
            val args = Bundle()
            args.putString("city", city)
            val fragment = DestinationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_destination, container, false)
    }

    private var activeCity: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDatastore = UserDatastore(requireContext())
        activeCity = arguments?.getString("city").orEmpty()
        loading = view.findViewById(R.id.loading)

        categoryAdapter = CategoryAdapter { categoryItem, i ->
            categoryAdapter.submitList(
                this.categories.toMutableList().mapIndexed { idx, item ->
                    if (idx == i) {
                        item.copy(
                            selected = !item.selected
                        )
                    } else {
                        item.copy(
                            selected = false
                        )
                    }
                }
            )
            adapter.submitList(
                this.places.toMutableList().filter { it.category == categoryItem.data }
            )
        }

        adapter = DestinationAdapter {
            val intent = Intent(requireContext(), DetailPlaceActivity::class.java)
            intent.putExtra("place", it)
            startActivity(intent)
        }

        rvPlace = view.findViewById(R.id.rvPlaces)
        rvPlace.layoutManager = GridLayoutManager(requireContext(), 2)
        rvPlace.adapter = adapter
        rvPlace.addItemDecoration(GridSpacingItemDecoration(2, 16, false))

        rvCategories = view.findViewById(R.id.rvCategories)
        rvCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvCategories.adapter = categoryAdapter

        refresh()
    }

    fun updateCity(city: String) {
        if (activeCity != city) {
            activeCity = city
            refresh()
        }
    }

    private fun refresh() {
        lifecycleScope.launch {
            val token = userDatastore.tokenFlow.firstOrNull()
            if (token == null) {
                Log.e("<TEST>", "Token not found")
                return@launch
            }

            try {
                loading.isVisible = true
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://ai-venture-cloud-computing-808736708163.asia-southeast2.run.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient(token))
                    .build()

                val service = retrofit.create(ApiService::class.java)
                val places = service.getPlaces()
                    .filter {
                        if (activeCity == "All") {
                            true
                        } else {
                            it.city == activeCity
                        }
                    }
                this@DestinationFragment.places = places.toMutableList()
                adapter.submitList(places)
                this@DestinationFragment.categories = places.groupBy { it.category }.keys.map {
                    CategoryItem(
                        selected = false,
                        data = it
                    )
                }.toMutableList()
                categoryAdapter.submitList(categories)
                loading.isVisible = false
            } catch (ex: Exception) {
                loading.isVisible = false
                Log.e("<TEST>", "Place API error: ${ex.stackTraceToString()}")
            }
        }
    }

    private fun provideOkHttpClient(token: String): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(ApiConfig.getLoggingInterceptor())
            .build()
    }
}