package com.example.kotlinflowplayground.repository.remote

import com.example.kotlinflowplayground.models.response.ActivityResponse
import com.example.kotlinflowplayground.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.*

interface ApiEndpoints {
    @GET("activity")
    suspend fun getCompletelyRandomActivity() : Response<ActivityResponse>

    @GET("activity?")
    suspend fun getARandomActivityWithMaxPrice(
        @Query(value= "minprice") minPrice : Float,
        @Query(value="maxprice") maxPrice : Float) : Response<ActivityResponse>

    @GET("activity?")
    suspend fun getARandomActivityWithManyParameters(
        @Query(value= "minparticipants") minParticipants : Float = 0.0f,
        @Query(value="maxparticipants") maxParticipants : Float = 100f,
        @Query(value= "minprice") minPrice : Float = 0.0f,
        @Query(value="maxprice") maxPrice : Float = 1.0f,
        @Query(value= "minaccessibility") minAccessibility : Float = 0.0f,
        @Query(value="maxaccessibility") maxAccessibility : Float = 1.0f
    ) : Response<ActivityResponse>
}