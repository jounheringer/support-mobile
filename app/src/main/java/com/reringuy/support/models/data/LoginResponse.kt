package com.reringuy.support.models.data

import com.reringuy.support.models.entities.User

data class LoginResponse(
    val user: User,
    val token: String
)
