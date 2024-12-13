package com.example.aiventure.data.service

import com.example.aiventure.camera.CameraRequest
import com.example.aiventure.camera.CameraResponse
import com.example.aiventure.camera.RecommendationImageResponse
import com.example.aiventure.camera.RecommendationResponse
import com.example.aiventure.category.datamodel.HotelItemResponse
import com.example.aiventure.category.datamodel.PlaceItemResponse
import com.example.aiventure.category.datamodel.RestaurantItemResponse
import com.example.aiventure.data.model.LoginRequest
import com.example.aiventure.data.model.LoginResponse
import com.example.aiventure.data.model.RegisterRequest
import com.example.aiventure.data.model.RegisterResponse
import com.example.aiventure.notifications.NotificationsResponse
import com.example.aiventure.otp.OtpRequest
import com.example.aiventure.recommendation.PlaceResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): LoginResponse

    @POST("scans")
    suspend fun createCamera(@Body request: CameraRequest): CameraResponse

    @POST("recommendation/images")
    suspend fun getRecommendations(
        @Body imageUrl: String
    ): Response<RecommendationResponse>

    @Multipart
    @POST("/scans")
    suspend fun cameraRequest(
        @Part image: MultipartBody.Part,
        @Part("cluster") cluster: RequestBody
    ): Response<CameraResponse>

    @Multipart
    @POST("/recommendation/images")
    suspend fun recommendationImages(
        @Part image: MultipartBody.Part
    ): Response<RecommendationImageResponse>

    @GET("/places/cluster/{cluster}")
    suspend fun getPlacesWithCluster(@Path("cluster") cluster: Int): List<PlaceResponse>

    @GET("/places")
    suspend fun getPlaces(): List<PlaceItemResponse>

    @GET("/hotels")
    suspend fun getHotels(): List<HotelItemResponse>

    @GET("/restaurants")
    suspend fun getRestaurants(): List<RestaurantItemResponse>

    @GET("/notifications")
    suspend fun getNotifications(): NotificationsResponse

    @POST("/auth/register/verify-otp")
    suspend fun verifyOtp(@Body otpRequest: OtpRequest)
}