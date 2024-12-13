package com.example.aiventure.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.aiventure.R
import com.example.aiventure.data.datastore.UserDatastore
import com.example.aiventure.data.service.ApiConfig
import com.example.aiventure.data.service.ApiService
import com.example.aiventure.notifications.NotificationActivity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator3
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var circleIndicator: CircleIndicator3
    private lateinit var tvCity: TextView

    private var cities: MutableList<String> = mutableListOf()

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var userDatastore: UserDatastore
    private lateinit var imageNotification: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val photoBtn = view.findViewById<View>(R.id.uploadImageBtn)
        photoBtn.setOnClickListener {
            findNavController().navigate(R.id.navigation_camera)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDatastore = UserDatastore(requireContext())

        viewPager = view.findViewById(R.id.viewPager)
        circleIndicator = view.findViewById(R.id.indicator)
        tvCity = view.findViewById(R.id.tvCity)
        imageNotification = view.findViewById(R.id.imageView4)
        imageNotification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        imageAdapter = ImageAdapter(requireContext())
        viewPager.adapter = imageAdapter
        viewPager.clipToPadding = false
        viewPager.setOffscreenPageLimit(3)
        viewPager.setPageTransformer(ScalePageTransformer())

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val selectedIndex = position
                tvCity.text = cities[selectedIndex]
            }
        })

        circleIndicator.setViewPager(viewPager)
        imageAdapter.registerAdapterDataObserver(circleIndicator.adapterDataObserver);

        loadPlace()
    }

    private fun loadPlace() {
        lifecycleScope.launch {
            try {
                val cluster = userDatastore.clusterKey.firstOrNull() ?: 1
                val token = userDatastore.tokenFlow.firstOrNull()
                if (token == null) {
                    Log.e("<TEST>", "Token not found")
                    return@launch
                }

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://ai-venture-cloud-computing-808736708163.asia-southeast2.run.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient(token))
                    .build()

                val service = retrofit.create(ApiService::class.java)
                val places = service.getPlacesWithCluster(cluster)
                val placesByCity = places.groupBy { it.city }
                    .mapValues {
                        it.value.take(3).map { place -> getFirstImage(place.imageUrl).trim('\'') }
                    }
                cities = placesByCity.keys.toMutableList()
                val images = placesByCity.values
                imageAdapter.submitList(images.toMutableList())
            } catch (ex: Exception) {
                Log.e("<TEST>", "Place API error: ${ex.stackTraceToString()}")
            }
        }
    }

    private fun getFirstImage(jsonArrayString: String): String {
        val cleanedString = jsonArrayString.trim('"')
        val urlArray = cleanedString.trim('[', ']').split(",")
        val firstUrl = urlArray[0].trim().trim('"')
        return firstUrl
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