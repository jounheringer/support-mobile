package com.reringuy.support.modules

import com.google.gson.Gson
import com.reringuy.support.enviroment.CustomEnviroment
import com.reringuy.support.services.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataServiceModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(CustomEnviroment.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))

    @Provides
    fun provideAuthService(okHttpClient: OkHttpClient, refrofit: Retrofit.Builder): AuthService =
        refrofit.baseUrl(CustomEnviroment.BASE_URL).client(okHttpClient).build()
            .create<AuthService>(AuthService::class.java)
}