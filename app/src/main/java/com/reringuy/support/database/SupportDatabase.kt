package com.reringuy.support.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.reringuy.support.database.dao.UserDao
import com.reringuy.support.models.entities.Task
import com.reringuy.support.models.entities.User

@Database(
    entities = [User::class, Task::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class SupportDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}