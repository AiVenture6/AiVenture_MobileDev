package com.example.aiventure.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.aiventure.R
import com.example.aiventure.data.AuthRepository
import com.example.aiventure.data.datastore.UserDatastore
import com.example.aiventure.data.model.RegisterRequest
import com.example.aiventure.otp.OtpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var authRepository: AuthRepository
    private val RC_SIGN_IN = 9001
    private lateinit var userDatastore: UserDatastore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        authRepository = AuthRepository(this)

        userDatastore = UserDatastore(this)

        // Initialize Google Sign-In
        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)

        val etEmail: TextInputEditText = findViewById(R.id.etEmail)
        val etPassword: TextInputEditText = findViewById(R.id.etPassword)
        val etUsername: TextInputEditText = findViewById(R.id.etUsername)
        val registerManualButton: Button = findViewById(R.id.btnRegister)
        val loginGoogleButton: Button = findViewById(R.id.btnGoogleLogin)
        val loginButton: TextView = findViewById(R.id.tvLogin)

        registerManualButton.setOnClickListener {
            try {
                lifecycleScope.launch {
                    val request = RegisterRequest(
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        etUsername.text.toString()
                    )
                    authRepository.registerUser(request)
                    signUpSuccess()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

        // Button Login Google
        loginGoogleButton.setOnClickListener {
            signInWithGoogle()
        }

        // Button Sign Up
        loginButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUpSuccess() {
        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@SignUpActivity, OtpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signInWithGoogle() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            // Login berhasil, lanjutkan ke halaman utama
            val intent = Intent(this@SignUpActivity, OtpActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: ApiException) {
            // Login gagal, tampilkan pesan error
            println("Google sign-in failed: ${e.statusCode}")
        }
    }
}