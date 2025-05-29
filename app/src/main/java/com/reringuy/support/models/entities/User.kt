package com.reringuy.support.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
class User (
    @PrimaryKey
    val id: Long,
    val email: String,
    val role: String,
)