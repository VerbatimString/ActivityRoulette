package com.example.kotlinflowplayground.repository.interfaces

import com.example.kotlinflowplayground.models.response.ActivityResponse
import com.example.kotlinflowplayground.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ActivityRepositoryBlueprint {
    suspend fun getCompletelyRandomActivity() : Response<ActivityResponse>
    suspend fun getRandomActivityWithMaxPrice(min : Float, max : Float) : Response<ActivityResponse>
    suspend fun getRandomActivityWithParameters(
        minParticipants : Float,
        maxParticipants : Float,
        minPrice : Float,
        maxPrice : Float,
        minAccessbility : Float,
        maxAccessibility : Float
    ) : Response<ActivityResponse>
}