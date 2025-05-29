package com.reringuy.support.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reringuy.support.database.dao.UserDao
import com.reringuy.support.models.entities.User

@Database(entities = [User::class], version = 1)
abstract class SupportDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}