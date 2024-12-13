package com.example.aiventure.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aiventure.R
import com.example.aiventure.data.datastore.UserDatastore
import com.example.aiventure.data.service.ApiConfig
import com.example.aiventure.data.service.ApiService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotificationActivity : AppCompatActivity() {

    private lateinit var rvNotification: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var userDatastore: UserDatastore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        userDatastore = UserDatastore(this)

        rvNotification = findViewById(R.id.rvNotification)
        ivBack = findViewById(R.id.ivBack)

        notificationAdapter = NotificationAdapter {
            val intent = Intent(this, NotificationDetailActivity::class.java)
            intent.putExtra("notification", it)
            startActivity(intent)
        }

        rvNotification.layoutManager = LinearLayoutManager(this)
        rvNotification.adapter = notificationAdapter

        ivBack.setOnClickListener {
            finish()
        }

        lifecycleScope.launch {
            val token = userDatastore.tokenFlow.firstOrNull()
            if (token == null) {
                Log.e("<TEST>", "Token not found")
                return@launch
            }
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://ai-venture-cloud-computing-808736708163.asia-southeast2.run.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient(token))
                    .build()

                val service = retrofit.create(ApiService::class.java)
                val notifications = service.getNotifications()
                notificationAdapter.updateData(notifications.data)
            } catch (ex: Exception) {
                Log.e("<RESULT>", ex.stackTraceToString())
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