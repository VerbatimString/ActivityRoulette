package com.example.kotlinflowplayground.di

import com.example.kotlinflowplayground.repository.ActivityRepositoryImpl
import com.example.kotlinflowplayground.repository.interfaces.ActivityRepositoryBlueprint
import com.example.kotlinflowplayground.repository.remote.ApiEndpointConstants
import com.example.kotlinflowplayground.repository.remote.ApiEndpoints
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(ApiEndpointConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiEndpoints(retrofit : Retrofit) : ApiEndpoints{
        return retrofit.create(ApiEndpoints::class.java)
    }


}