package com.reringuy.support

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.reringuy.support.auth.biometric.BiometricPromptManager
import com.reringuy.support.presentation.screens.login.LoginScreenWrapper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val biometricPromptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScreenWrapper(biometricPromptManager) {
                handleOnLogin()
            }
        }
    }

    private fun handleOnLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}