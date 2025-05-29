package com.reringuy.support.modules

import android.content.Context
import com.reringuy.support.auth.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SingletonModules {

    @Provides
    fun provideTokenManager(@ApplicationContext context: Context) = TokenManager(context)
}