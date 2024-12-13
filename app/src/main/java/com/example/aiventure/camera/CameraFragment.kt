package com.example.aiventure.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.example.aiventure.R
import com.example.aiventure.data.datastore.UserDatastore
import com.example.aiventure.data.service.ApiConfig
import com.example.aiventure.data.service.ApiService
import com.example.aiventure.recommendation.CameraResultActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CameraFragment : Fragment() {
    private lateinit var viewFinder: PreviewView
    private var imageCapture: ImageCapture? = null
    private lateinit var userDatastore: UserDatastore
    private lateinit var flLoading: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        userDatastore = UserDatastore(requireContext()) // Inisialisasi UserDatastore

        requestCameraPermissions()

        viewFinder = view.findViewById(R.id.viewFinder)

        val btn = view.findViewById<ImageView>(R.id.image_capture_button)
        btn.setOnClickListener {
            takePhoto()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flLoading = view.findViewById(R.id.fl_loading)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            requireContext().externalCacheDir,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.d(TAG, "Photo capture succeeded: ${photoFile.absolutePath}")
                    flLoading.isVisible = true
                    sendToApi(photoFile)
                }
            }
        )
    }

    private fun sendToApi(file: File) {
        lifecycleScope.launch {
            // Ambil token secara asinkron
            val token = userDatastore.tokenFlow.firstOrNull()
            if (token == null) {
                Log.e(TAG, "Token not found")
                return@launch
            }

            Log.d("<RESULT>", token)

            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val clusterPart = RequestBody.create("text/plain".toMediaTypeOrNull(), "2")

            val retrofit = Retrofit.Builder()
                .baseUrl("https://ai-venture-cloud-computing-808736708163.asia-southeast2.run.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(provideOkHttpClient(token))
                .build()

            val service = retrofit.create(ApiService::class.java)

            try {
                val response = service.cameraRequest(body, clusterPart)
                if (response.isSuccessful) {
                    processImageWithMLApi(file, requestFile, token)
                } else {
                    Log.e(TAG, "Camera API error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                flLoading.isVisible = false
            }
        }
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
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun processImageWithMLApi(file: File, requestFile: RequestBody, token: String) {
        lifecycleScope.launch {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api-backend-ml-808736708163.asia-southeast2.run.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient(token))
                    .build()

                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val service = retrofit.create(ApiService::class.java)
                val response = service.recommendationImages(body)

                if (response.isSuccessful) {
                    val recommendation = response.body()
                    Log.d(TAG, "Recommendation received: $recommendation")
                    if (recommendation?.status == "Success") {
                        val intent = Intent(requireContext(), CameraResultActivity::class.java)
                        intent.putExtra("cluster", recommendation.cluster)
                        startActivity(intent)
                    } else {
                       Log.e(TAG, "Error")
                    }
                } else {
                    Log.e(TAG, "ML API error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            flLoading.isVisible = false
        }
    }

    private fun requestCameraPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS)
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                // Handle permission denial
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "CameraFragment"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
