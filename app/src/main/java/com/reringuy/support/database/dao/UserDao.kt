package com.reringuy.support.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reringuy.support.models.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM usuarios")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Long

    @Delete
    fun deleteUser(user: User)
}