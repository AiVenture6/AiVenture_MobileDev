package com.example.aiventure.recommendation

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aiventure.R
import com.example.aiventure.data.datastore.UserDatastore
import com.example.aiventure.data.service.ApiConfig
import com.example.aiventure.data.service.ApiService
import com.example.aiventure.detail.DetailActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CameraResultActivity : AppCompatActivity() {

    private lateinit var rvResult: RecyclerView
    private lateinit var userDatastore: UserDatastore
    private lateinit var placeAdapter: PlaceAdapter

    private lateinit var btnAll: Button
    private lateinit var btnJakarta: Button
    private lateinit var btnSemarang: Button
    private lateinit var btnBandung: Button
    private lateinit var btnYogyakarta: Button
    private lateinit var btnSurabaya: Button

    private lateinit var priceChip: Chip
    private lateinit var ratingChip: Chip
    private lateinit var nearestChip: Chip
    private lateinit var chipGroup: ChipGroup

    private var isPriceAscending = true
    private var isRatingAscending = true

    private var places: MutableList<PlaceResponse> = mutableListOf()
    private var shownList: MutableList<PlaceResponse> = mutableListOf()
    private var activeCity: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_result)

        btnAll = findViewById(R.id.btnAll)
        btnJakarta = findViewById(R.id.btnJakarta)
        btnSemarang = findViewById(R.id.btnSemarang)
        btnBandung = findViewById(R.id.btnBandung)
        btnYogyakarta = findViewById(R.id.btnYogyakarta)
        btnSurabaya = findViewById(R.id.btnSurabaya)

        chipGroup = findViewById(R.id.chip_group)
        priceChip = findViewById(R.id.price)
        ratingChip = findViewById(R.id.rating)
        nearestChip = findViewById(R.id.nearest)

        priceChip.setOnClickListener {
            togglePriceSort()
        }

        ratingChip.setOnClickListener {
            toggleRatingSort()
        }

        nearestChip.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isChecked) {
                placeAdapter.submitList(this.places.toMutableList().sortedBy { it.distance })
            } else {
                placeAdapter.submitList(this.places.toMutableList())
            }
        }

        btnAll.setOnClickListener {
            shownList = this.places.toMutableList()
            placeAdapter.submitList(shownList)
            activeCity = "All"
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            refreshFilter()
        }

        btnJakarta.setOnClickListener {
            shownList = this.places.toMutableList().filter { it.city == "Jakarta" }.toMutableList()
            placeAdapter.submitList(shownList)
            activeCity = "Jakarta"
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSurabaya.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnYogyakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            refreshFilter()
        }

        btnSemarang.setOnClickListener {
            shownList = this.places.toMutableList().filter { it.city == "Semarang" }.toMutableList()
            placeAdapter.submitList(shownList)
            activeCity = "Semarang"
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSurabaya.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnYogyakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            refreshFilter()
        }

        btnBandung.setOnClickListener {
            shownList = this.places.toMutableList().filter { it.city == "Bandung" }.toMutableList()
            placeAdapter.submitList(shownList)
            activeCity = "Semarang"
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            btnSurabaya.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnYogyakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            refreshFilter()
        }

        btnSurabaya.setOnClickListener {
            shownList = this.places.toMutableList().filter { it.city == "Surabaya" }.toMutableList()
            placeAdapter.submitList(shownList)
            activeCity = "Surabaya"
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSurabaya.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            btnYogyakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            refreshFilter()
        }

        btnYogyakarta.setOnClickListener {
            shownList =
                this.places.toMutableList().filter { it.city == "Yogyakarta" }.toMutableList()
            placeAdapter.submitList(shownList)
            activeCity = "Yogyakarta"
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSurabaya.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnYogyakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            refreshFilter()
        }

        rvResult = findViewById(R.id.rvResults)
        placeAdapter = PlaceAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("place", it)
            startActivity(intent)
        }
        userDatastore = UserDatastore(this)

        val cluster = intent?.getIntExtra("cluster", 0) ?: 0

        Toast.makeText(this, cluster.toString(), Toast.LENGTH_SHORT).show()
        rvResult.layoutManager = GridLayoutManager(this, 2)
        rvResult.adapter = placeAdapter
        rvResult.addItemDecoration(GridSpacingItemDecoration(2, 16, false))

        lifecycleScope.launch {
            val token = userDatastore.tokenFlow.firstOrNull()
            if (token == null) {
                Log.e("<TEST>", "Token not found")
                return@launch
            }

            Log.d("<RESULT>", token)

            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://ai-venture-cloud-computing-808736708163.asia-southeast2.run.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient(token))
                    .build()

                val service = retrofit.create(ApiService::class.java)
                val places = service.getPlacesWithCluster(cluster).map {
                    it.copy(
                        imageUrl = getFirstImage(it.imageUrl)?.trim('\'').orEmpty()
                    )
                }
                this@CameraResultActivity.places = places.toMutableList()
                this@CameraResultActivity.shownList = this@CameraResultActivity.places
                placeAdapter.submitList(places)
            } catch (ex: Exception) {
                Log.e("<TEST>", "Place API error: ${ex.stackTraceToString()}")
            }
        }
    }

    private fun refreshFilter() {
        filterPrice()
        filterRating()
        nearestChip.isChecked = false
    }

    private fun filterPrice() {
        if (isPriceAscending) {
            placeAdapter.submitList(shownList.toMutableList().sortedBy { it.priceRange })
        } else {
            placeAdapter.submitList(shownList.toMutableList().sortedByDescending { it.priceRange })
        }
    }

    private fun filterRating() {
        if (isRatingAscending) {
            placeAdapter.submitList(
                shownList.toMutableList().sortedBy { it.ratings.firstOrNull() ?: 0f })
        } else {
            placeAdapter.submitList(
                shownList.toMutableList().sortedByDescending { it.ratings.firstOrNull() ?: 0f })
        }
    }

    private fun togglePriceSort() {
        if (isPriceAscending) {
            priceChip.chipIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
        } else {
            priceChip.chipIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_up)
        }
        isPriceAscending = !isPriceAscending
        filterPrice()
    }

    private fun toggleRatingSort() {
        if (isRatingAscending) {
            ratingChip.chipIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
        } else {
            ratingChip.chipIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_up)
        }
        isRatingAscending = !isRatingAscending
        filterRating()
    }

    private fun getFirstImage(jsonArrayString: String): String? {
        val cleanedString = jsonArrayString.trim('"')
        val urlArray = cleanedString.trim('[', ']').split(",")
        val firstUrl = urlArray[0].trim().trim('"')
        return firstUrl
    }

    private fun provideOkHttpClient(token: String): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")  // Tambahkan token di header
                .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(ApiConfig.getLoggingInterceptor())
            .build()
    }
}