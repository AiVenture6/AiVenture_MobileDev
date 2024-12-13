package com.example.aiventure.otp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.aiventure.MainActivity
import com.example.aiventure.R
import com.example.aiventure.data.datastore.UserDatastore
import com.example.aiventure.data.service.ApiConfig
import com.example.aiventure.data.service.ApiService
import com.example.aiventure.preference.PreferenceActivity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OtpActivity : AppCompatActivity() {
    private lateinit var userDatastore: UserDatastore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        userDatastore = UserDatastore(this)

        val otpDigit1 = findViewById<EditText>(R.id.otp_digit1)
        val otpDigit2 = findViewById<EditText>(R.id.otp_digit2)
        val otpDigit3 = findViewById<EditText>(R.id.otp_digit3)
        val otpDigit4 = findViewById<EditText>(R.id.otp_digit4)
        val otpDigit5 = findViewById<EditText>(R.id.otp_digit5)
        val otpDigit6 = findViewById<EditText>(R.id.otp_digit6)
        val btnVerify = findViewById<Button>(R.id.btn_verify)

        btnVerify.setOnClickListener {
            val otp = otpDigit1.text.toString() +
                    otpDigit2.text.toString() +
                    otpDigit3.text.toString() +
                    otpDigit4.text.toString() +
                    otpDigit5.text.toString() +
                    otpDigit6.text.toString()

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
                    service.verifyOtp(OtpRequest(otp))
                    Toast.makeText(this@OtpActivity, "Verify Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@OtpActivity, PreferenceActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (ex: Exception) {
                    Toast.makeText(this@OtpActivity, "OTP Incorrect", Toast.LENGTH_SHORT).show()
                    Log.e("<TEST>", "Place API error: ${ex.stackTraceToString()}")
                }
            }

            if (otp.length == 6) {
                Toast.makeText(this, "OTP Entered: $otp", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Please enter all digits of OTP", Toast.LENGTH_SHORT).show()
            }
        }

        otpDigit1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) otpDigit2.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        otpDigit2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) otpDigit3.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        otpDigit3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) otpDigit4.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        otpDigit4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) otpDigit5.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        otpDigit5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) otpDigit6.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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
