package com.example.kotlinflowplayground.di

import com.example.kotlinflowplayground.repository.ActivityRepositoryImpl
import com.example.kotlinflowplayground.repository.interfaces.ActivityRepositoryBlueprint
import com.example.kotlinflowplayground.usecases.GetActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@InstallIn(ViewModelComponent::class)
@Module
class UseCaseModule {
    @Provides
    fun provideGetActivityUseCase(activityRepositoryBlueprint: ActivityRepositoryBlueprint) : GetActivity{
        return GetActivity(activityRepositoryBlueprint)
    }
}