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
import androidx.recyclerview.widget.RecyclerView
import com.example.aiventure.R
import com.example.aiventure.category.adapter.HotelAdapter
import com.example.aiventure.category.datamodel.HotelItemResponse
import com.example.aiventure.category.detail.DetailHotelActivity
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

class HotelFragment : Fragment() {
    private var hotels: MutableList<HotelItemResponse> = mutableListOf()
    private lateinit var userDatastore: UserDatastore
    private lateinit var adapter: HotelAdapter
    private lateinit var rvHotel: RecyclerView
    private lateinit var loading: ProgressBar

    companion object {
        fun newInstance(city: String): HotelFragment {
            val args = Bundle()
            args.putString("city", city)
            val fragment = HotelFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun updateCity(city: String) {
        if (activeCity != city) {
            activeCity = city
            refresh()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hotel, container, false)
    }

    private var activeCity: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDatastore = UserDatastore(requireContext())
        activeCity = arguments?.getString("city").orEmpty()

        loading = view.findViewById(R.id.loading)

        adapter = HotelAdapter {
            val intent = Intent(requireContext(), DetailHotelActivity::class.java)
            intent.putExtra("place", it)
            startActivity(intent)
        }

        rvHotel = view.findViewById(R.id.rvHotels)
        rvHotel.layoutManager = GridLayoutManager(requireContext(), 2)
        rvHotel.adapter = adapter
        rvHotel.addItemDecoration(GridSpacingItemDecoration(2, 16, false))

        refresh()
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
                val places = service.getHotels().filter {
                    if (activeCity == "All") {
                        true
                    } else {
                        it.city == activeCity
                    }
                }.toMutableList()
                this@HotelFragment.hotels = places
                adapter.submitList(places)
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