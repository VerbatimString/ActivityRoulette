package com.example.kotlinflowplayground.usecases

import com.example.kotlinflowplayground.models.response.ActivityResponse
import com.example.kotlinflowplayground.repository.interfaces.ActivityRepositoryBlueprint
import com.example.kotlinflowplayground.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetActivity @Inject constructor(private val activityRepository : ActivityRepositoryBlueprint) {
    suspend operator fun invoke() : Flow<NetworkResult<ActivityResponse>>{
        return flow{
            //No parameter hence completely random activity
            val response = activityRepository.getCompletelyRandomActivity()

            if(response.isSuccessful){
                val data = response.body()!!
                emit(NetworkResult.Success(data))
            }

            else {
                val message = "Failed to get an activity :("
                var data : ActivityResponse? = null
                if(response.body() != null)
                    data = response.body()

                emit(NetworkResult.Error(data!!, message))
            }
        }
    }

    suspend operator fun invoke(minValue : Float = 0.0f, maxValue : Float) : Flow<NetworkResult<ActivityResponse>>{
        return flow{
            val response = activityRepository.getRandomActivityWithMaxPrice(minValue, maxValue)

            if(response.isSuccessful && response.body() != null){
                val data = response.body()!!
                emit(NetworkResult.Success(data))
            }

            else {
                val message = "Failed to get an activity :("
                var data : ActivityResponse? = null
                if(response.body() != null)
                    data = response.body()

                emit(NetworkResult.Error(data!!, message))
            }
        }
    }

    suspend operator fun invoke(
        minParticipants : Float,
        maxParticipants : Float,
        minPrice : Float,
        maxPrice : Float,
        minAccessiblity : Float,
        maxAccessibility : Float
    ) : Flow<NetworkResult<ActivityResponse>>{
        return flow{
            val response = activityRepository.getRandomActivityWithParameters(
                minParticipants, maxParticipants,
            minPrice, maxPrice,
            minAccessiblity, maxAccessibility)

            if(response.isSuccessful && response.body()?.error == null){
                val data = response.body()!!
                emit(NetworkResult.Success(data))
            }

            else {
                var message = "Failed to get an activity :("
                var data = response?.body()
                if(response.body() != null){
                    data = response.body()
                    if(response.body()!!.error != null)
                        message = response.body()!!.error!!

                }

                emit(NetworkResult.Error(data!!, message))
            }
        }
    }
}