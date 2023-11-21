package com.example.kotlinflowplayground.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinflowplayground.models.response.ActivityResponse
import com.example.kotlinflowplayground.usecases.GetActivity
import com.example.kotlinflowplayground.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val getActivity : GetActivity
) : ViewModel() {

    private val _randomActivity = MutableLiveData<NetworkResult<ActivityResponse>>(null)
    val randomActivity : LiveData<NetworkResult<ActivityResponse>> = _randomActivity

    fun getCompletelyRandomActivity() {
        //Check for network, return error channel if network not available
        _randomActivity.postValue(NetworkResult.Loading())

        viewModelScope.launch {
            getActivity().collect(){
                _randomActivity.postValue(it)
            }
        }
    }

    fun getRandomActivityWithAMaxPrice(max : Float){
        _randomActivity.postValue(NetworkResult.Loading())

        viewModelScope.launch {
            getActivity(0.0f, max).collect(){
                _randomActivity.postValue(it)
            }
        }
    }

    fun getRandomActivityWithManyParameters(
        minParticipants : Float = 0.0f,
        maxParticipants : Float = 100f,
        minPrice : Float = 0.0f,
        maxPrice : Float = 1.0f,
        minAccessibliity : Float = 0.0f,
        maxAccessibility : Float = 1.0f
    ){
        _randomActivity.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            getActivity(
                minParticipants,
                maxParticipants,
                minPrice,
                maxPrice,
                minAccessibliity,
                maxAccessibility
            ).collect(){
                _randomActivity.postValue(it)
            }
        }
    }
}