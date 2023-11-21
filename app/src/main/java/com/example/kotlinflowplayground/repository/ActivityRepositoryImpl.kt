package com.example.kotlinflowplayground.repository

import com.example.kotlinflowplayground.models.response.ActivityResponse
import com.example.kotlinflowplayground.repository.interfaces.ActivityRepositoryBlueprint
import com.example.kotlinflowplayground.repository.remote.ApiEndpoints
import com.example.kotlinflowplayground.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(private val apiEndpoints: ApiEndpoints)
    : ActivityRepositoryBlueprint{
    override suspend fun getCompletelyRandomActivity(): Response<ActivityResponse>{
        return apiEndpoints.getCompletelyRandomActivity()
    }

    override suspend fun getRandomActivityWithMaxPrice(
        min: Float,
        max: Float
    ): Response<ActivityResponse> {
        return apiEndpoints.getARandomActivityWithMaxPrice(0.0f, max)
    }

    override suspend fun getRandomActivityWithParameters(
        minParticipants: Float,
        maxParticipants: Float,
        minPrice: Float,
        maxPrice: Float,
        minAccessbility: Float,
        maxAccessibility: Float
    ): Response<ActivityResponse> {
        return apiEndpoints.getARandomActivityWithManyParameters(
            minParticipants,
            maxParticipants,
            minPrice,
            maxPrice,
            minAccessbility,
            maxAccessibility
        )
    }
}