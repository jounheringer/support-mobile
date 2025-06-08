package com.reringuy.support.services

import com.reringuy.support.models.data.EmailPassword
import com.reringuy.support.models.data.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth")
    suspend fun login(@Body authData: EmailPassword): Response<LoginResponse>
}