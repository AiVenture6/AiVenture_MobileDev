package com.example.aiventure.data

import android.content.Context
import com.example.aiventure.data.datastore.UserDatastore
import com.example.aiventure.data.model.LoginRequest
import com.example.aiventure.data.model.RegisterRequest
import com.example.aiventure.data.service.ApiConfig
import com.example.aiventure.data.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class AuthRepository(
    private val context: Context
) {
    private val apiService: ApiService = ApiConfig.getApiService()
    private val userDatastore: UserDatastore = UserDatastore(context)

    suspend fun registerUser(request: RegisterRequest) {
        withContext(Dispatchers.IO) {
            val response = apiService.registerUser(request)
            userDatastore.saveToken(response.token, request.email, request.name)
        }
    }

    suspend fun loginUser(request: LoginRequest) {
        val response = apiService.loginUser(request)
        userDatastore.saveToken(response.token, response.user.email, response.user.name)
    }

    suspend fun isAuthenticated(): Boolean {
        return userDatastore.tokenFlow.firstOrNull() != null
    }
}