package com.example.kotlinflowplayground.di

import com.example.kotlinflowplayground.repository.ActivityRepositoryImpl
import com.example.kotlinflowplayground.repository.interfaces.ActivityRepositoryBlueprint
import com.example.kotlinflowplayground.repository.remote.ApiEndpoints
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideActivityRepository(apiEndpoints : ApiEndpoints) : ActivityRepositoryBlueprint {
        return ActivityRepositoryImpl(apiEndpoints)
    }
}