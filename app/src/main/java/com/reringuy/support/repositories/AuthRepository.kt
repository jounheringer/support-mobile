package com.reringuy.support.repositories

import android.util.Log
import com.reringuy.support.models.data.EmailPassword
import com.reringuy.support.services.AuthService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
) {
    fun login(emailPassword: EmailPassword) = flow {
        val response = authService.login(emailPassword)
        Log.d("ble.1", response.toString())
        Log.d("ble.2", response.message())
        if (response.isSuccessful) {
            emit(response.body())
        } else {
            emit(null)
        }
    }
}