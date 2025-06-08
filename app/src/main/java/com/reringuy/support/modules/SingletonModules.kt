package com.reringuy.support.modules

import android.content.Context
import com.google.gson.Gson
import com.reringuy.support.auth.AuthInterceptor
import com.reringuy.support.auth.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModules {

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context) = TokenManager(context)

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager) = AuthInterceptor(tokenManager)
}