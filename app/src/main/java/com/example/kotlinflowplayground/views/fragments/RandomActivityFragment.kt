package com.example.kotlinflowplayground.views.fragments

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.Transition
import com.example.kotlinflowplayground.R
import com.example.kotlinflowplayground.databinding.FragmentRandomActivityBinding
import com.example.kotlinflowplayground.models.ParcelableKeyConstants
import com.example.kotlinflowplayground.models.response.ActivityResponse
import com.example.kotlinflowplayground.utils.NetworkResult
import com.example.kotlinflowplayground.viewmodels.ActivityViewModel
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFade
import dagger.hilt.android.AndroidEntryPoint
import com.example.kotlinflowplayground.utils.AnimationHelper
import com.example.kotlinflowplayground.utils.AnimationHelper.Companion.enterFromLeftAnimation
import com.example.kotlinflowplayground.utils.AnimationHelper.Companion.enterFromRightAnimation
import com.example.kotlinflowplayground.utils.AnimationHelper.Companion.enterFromBottomLeft
import com.example.kotlinflowplayground.utils.AnimationHelper.Companion.hideViews
import com.example.kotlinflowplayground.utils.AnimationHelper.Companion.showViews



import java.util.logging.Logger

@AndroidEntryPoint
class RandomActivityFragment : Fragment() {
    var _binding : FragmentRandomActivityBinding? = null
    val binding get() = _binding!!
    val activityViewModel by viewModels<ActivityViewModel>()

    var currentAccessibilitySliderValue : Float = 1.0f
    var currentPriceSliderValue : Float = 100f
    var currentParticipantsSliderValue : Float  = 1.0f

    companion object{
        val TAG = RandomActivityFragment::class.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRandomActivityBinding.inflate(inflater, container, false)
        setListeners()
        setObservers()

        AnimationHelper.initialize(requireContext())

        sharedElementEnterTransition = MaterialContainerTransform(requireContext(), true).apply {
            drawingViewId = R.id.getRandomThought_iv
            scrimColor = Color.TRANSPARENT
            duration = 300
            interpolator = FastOutSlowInInterpolator()
        }

        sharedElementReturnTransition = MaterialContainerTransform(requireContext(), false).apply {
            scrimColor = Color.TRANSPARENT
            duration = 300
            interpolator = FastOutSlowInInterpolator()
        }

        (sharedElementEnterTransition as MaterialContainerTransform).addListener(
            object: Transition.TransitionListener
        {
            override fun onTransitionEnd(transition: Transition) {
                enterFromRightAnimation(
                    arrayListOf<View>(binding.moneySlider, binding.accessibilitySlider, binding.participantsSlider,
                        binding.moneySliderStringTv,
                        binding.participantsSliderStringTv,
                        binding.accessibilitySliderStringTv))

                enterFromLeftAnimation(
                    arrayListOf<View>(
                        binding.moneySliderValueTv,
                        binding.accessibilitySliderValueTv,
                        binding.participantsValueTv)
                )

                enterFromBottomLeft(arrayListOf<View>(binding.pointerToButtonIv))

                showViews(
                    arrayListOf(
                        binding.moneySlider,
                        binding.participantsSlider,
                        binding.accessibilitySlider,
                        binding.moneySliderStringTv,
                        binding.participantsSliderStringTv,
                        binding.accessibilitySliderStringTv,
                        binding.moneySliderValueTv,
                        binding.participantsValueTv,
                        binding.accessibilitySliderValueTv,
                        binding.pointerToButtonIv
                    )
                )
            }

            override fun onTransitionResume(transition: Transition) {
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionCancel(transition: Transition) {
            }

            override fun onTransitionStart(transition: Transition) {

                hideViews(
                    arrayListOf(
                        binding.moneySlider,
                                binding.participantsSlider,
                                binding.accessibilitySlider,
                                binding.moneySliderStringTv,
                                binding.participantsSliderStringTv,
                                binding.accessibilitySliderStringTv,
                                binding.moneySliderValueTv,
                                binding.participantsValueTv,
                                binding.accessibilitySliderValueTv,
                                binding.pointerToButtonIv
                    )
                )
            }

        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setListeners(){

        binding.accessibilitySliderValueTv.text = null
        binding.moneySliderValueTv.text = null
        binding.participantsValueTv.text = null

        binding.accessibilitySlider.addOnChangeListener { slider, value, fromUser ->
            currentAccessibilitySliderValue = if(value > 0.0f) value else 1.0f
            binding.accessibilitySliderValueTv.text =  convertAccessibilityFloatIntoRelatedString(
                value
            )
        }

        binding.moneySlider.addOnChangeListener { slider, value, fromUser ->
            currentPriceSliderValue = if(value > 0.0f) value else 100f
            binding.moneySliderValueTv.text = if(value > 0) (Math.round(value)).toString() + "$" else "Any"
        }

        binding.participantsSlider.addOnChangeListener { slider, value, fromUser ->
            currentParticipantsSliderValue = if(value > 0) value else 100f
            binding.participantsValueTv.text = if(value > 0) getParticipantText(value) else "Any"
        }

        binding.getRandomThoughtIv.setOnClickListener {
            getARandomThought()
        }
    }

    override fun onResume() {
        super.onResume()
        enableUiAndStopLoadingAnim()
    }

    override fun onDestroy() {
        activityViewModel.randomActivity.removeObservers(viewLifecycleOwner)
        super.onDestroy()
    }

    fun setObservers(){
        activityViewModel.randomActivity.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    //Goto next fragment
                        switchToExpandedActivityFragment(it.data!!)
                        Log.d(TAG, "setObservers: received network success")
                        Log.d(TAG, "setObservers: " + it.data.toString())
                }

                is NetworkResult.Error -> {
                    enableUiAndStopLoadingAnim()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "setObservers: received network error")
                    Log.d(TAG, "setObservers: " + it.message)
                }

                is NetworkResult.Loading -> {
                    disableUiAndStartLoadingAnim()
                    Log.d(TAG, "setObservers: received network loading")
                }
            }
        })
    }

    fun getARandomThought(){

        if(currentPriceSliderValue == 1.0f &&
            currentAccessibilitySliderValue == 1.0f &&
                    currentParticipantsSliderValue == 1.0f
        ){
            getCompletelyRandomThought()
        }


        else{
            val accessibilityPair = convertAccessiblityStringToAMinMaxPair(binding.accessibilitySliderValueTv.text.toString())
            Log.d(TAG, "Sending request for the following variables: ")
            Log.d(TAG, "Accessibility: ${accessibilityPair.first} to ${accessibilityPair.second}")
            Log.d(TAG, "Price: ${0.0f} to ${currentPriceSliderValue/100f}")
            Log.d(TAG, "Participants: ${ if (currentParticipantsSliderValue == 100f) 0f else currentParticipantsSliderValue} to ${currentParticipantsSliderValue}")

            getAThoughtWithMultipleParameters(
                if (currentParticipantsSliderValue == 100f) 0f else currentParticipantsSliderValue,
                currentParticipantsSliderValue,
                0.0f,
                (currentPriceSliderValue)/100f,
                accessibilityPair.first,
                accessibilityPair.second
            )
        }
    }

    fun getCompletelyRandomThought(){
        activityViewModel.getCompletelyRandomActivity()
    }

    fun getAThoughtWithMultipleParameters(
        minParticipants : Float = 0.0f,
        maxParticipants : Float = 100f,
        minPrice : Float = 0.0f,
        maxPrice : Float = 1.0f,
        minAccessibliity : Float = 0.0f,
        maxAccessibility : Float = 1.0f
    ){
        activityViewModel.getRandomActivityWithManyParameters(
            minParticipants,
            maxParticipants,
            minPrice,
            maxPrice,
            minAccessibliity,
            maxAccessibility
        )
    }

    fun convertAccessibilityFloatIntoRelatedString(currentAccessibilitySliderValue : Float) : String{

        if(currentAccessibilitySliderValue == 0.0f){
            return "Any"
        }

        if(currentAccessibilitySliderValue == 1.0f){
            return "None"
        }

        if(currentAccessibilitySliderValue == 2.0f){
            return "Slight"
        }

        if(currentAccessibilitySliderValue == 3.0f){
            return "A lot"
        }

        return ""
    }

    fun convertAccessiblityStringToAMinMaxPair(accessibilityString : String) : Pair<Float, Float>{

        if(accessibilityString == "Any" || accessibilityString == "null"){
            return Pair(0.0f, 1.0f)
        }

        if(accessibilityString == "None"){
            return Pair(0.0f, 0.33f)
        }

        if(accessibilityString == "Slight"){
            return Pair(0.33f, 0.66f)
        }

        if(accessibilityString == "A lot"){
            return Pair(0.66f, 1.0f)
        }

        return Pair(0.0f, 1.0f)
    }

    fun getParticipantText(value : Float) : String{
        when(value){
            1.0f -> return "Just you"
            2.0f -> return "Two people"
            3.0f -> return "Three people"
            4.0f -> return "Big squad"
        }

        return "Any"
    }

    fun switchToExpandedActivityFragment(activityResponse : ActivityResponse){
        val directions = RandomActivityFragmentDirections.
        actionRandomActivityFragmentToRandomActivityExpanded(activityResponse)
        val transitionFrom = binding.getRandomThoughtIv
        val transitionTo = getString(R.string.expanded_activity_transition_name)
        val extras = FragmentNavigatorExtras(transitionFrom to transitionTo)
        findNavController().navigate(directions, extras)
    }

    fun disableUiAndStartLoadingAnim(){
        binding.getRandomThoughtIv.isEnabled = false
        binding.getRandomThoughtIv.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.button_loading_anim).apply {
                repeatCount = Animation.INFINITE
            }
        )
    }

    fun enableUiAndStopLoadingAnim(){
        binding.getRandomThoughtIv.isEnabled = true
        binding.getRandomThoughtIv.clearAnimation()
    }
}