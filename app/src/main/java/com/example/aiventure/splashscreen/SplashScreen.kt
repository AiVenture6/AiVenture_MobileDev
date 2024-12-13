package com.example.aiventure.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.aiventure.MainActivity
import com.example.aiventure.R
import com.example.aiventure.data.AuthRepository
import com.example.aiventure.data.datastore.UserDatastore
import com.example.aiventure.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var authRepository: AuthRepository
    private lateinit var userDatastore: UserDatastore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)
        userDatastore = UserDatastore(this)

        lifecycleScope.launch {
            userDatastore.darkModeFlow.collect { isDarkMode ->
                applyDarkMode(isDarkMode)
            }
        }

        authRepository = AuthRepository(this)

        lifecycleScope.launch {
            delay(1000)
            when (authRepository.isAuthenticated()) {
                true -> toMain()
                false -> toLogin()
            }
        }
    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Tutup Splash Screen agar tidak bisa kembali
    }

    private fun toMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Tutup Splash Screen agar tidak bisa kembali
    }

    private fun applyDarkMode(isDarkMode: Boolean) {
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}