package com.reringuy.support.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.google.gson.Gson
import com.reringuy.support.models.entities.User


class TokenManager (
    private val context: Context
) {
    companion object {
        val USER_KEY = stringPreferencesKey("user_key")
        val TOKEN_KEY = stringPreferencesKey("token_key")
    }
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

    suspend fun saveToken(token: String) {
        context.dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit {
            it[USER_KEY] = Gson().toJson(user)
        }
    }

    fun getCurrentUser(): Flow<User?> {
        return context.dataStore.data.map {
            Gson().fromJson(it[USER_KEY], User::class.java)
        }
    }

    fun getCurrentToken(): Flow<String?> {
        return context.dataStore.data.map {
            it[TOKEN_KEY]
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit {
            it.remove(USER_KEY)
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit {
            it.remove(TOKEN_KEY)
        }
    }

    suspend fun clearAll() {
        clearToken()
        clearUser()
    }

}