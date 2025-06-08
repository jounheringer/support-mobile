package com.reringuy.support.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.reringuy.support.auth.AuthInterceptor
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
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class DataServiceModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(CustomEnviroment.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))

    @Provides
    fun provideAuthService(): AuthService {
        val client = OkHttpClient.Builder().connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(CustomEnviroment.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))

        return retrofit.build().create(AuthService::class.java)
    }
}