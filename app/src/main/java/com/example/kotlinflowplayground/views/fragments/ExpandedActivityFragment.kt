package com.example.kotlinflowplayground.views.fragments

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.activity.addCallback
import androidx.core.animation.doOnEnd
import androidx.core.transition.addListener
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.Transition
import com.example.kotlinflowplayground.R
import com.example.kotlinflowplayground.databinding.FragmentRandomActivityExpandedBinding
import com.example.kotlinflowplayground.models.ParcelableKeyConstants
import com.example.kotlinflowplayground.models.response.ActivityResponse
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis

class RandomActivityExpanded : Fragment() {
    var _binding : FragmentRandomActivityExpandedBinding? = null
    val binding get() = _binding!!

    var deviceWidth : Float = 0.0f
    var deviceHeight : Float = 0.0f

    val args : RandomActivityExpandedArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        deviceWidth = requireContext().resources.displayMetrics.widthPixels.toFloat()
        deviceHeight = requireContext().resources.displayMetrics.heightPixels.toFloat()

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

        _binding = FragmentRandomActivityExpandedBinding.inflate(inflater, container, false)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            returnAnimations()
        }

        unpackParcel()
        binding.activityPriceTv.visibility = View.INVISIBLE
        binding.activityDisplayTv.visibility = View.INVISIBLE
        binding.activityAccessibilityTv.visibility = View.INVISIBLE
        binding.activityParticipantsTv.visibility = View.INVISIBLE

        (sharedElementEnterTransition as MaterialContainerTransform).addListener(object: Transition.TransitionListener
        {
            override fun onTransitionEnd(transition: Transition) {

                val startYActivityDisplay = deviceHeight.toFloat()
                val endYActivityDisplay = binding.activityDisplayTv.y.toFloat()

                ObjectAnimator.ofFloat(
                    binding.activityDisplayTv,
                    View.Y,
                    startYActivityDisplay,
                    endYActivityDisplay)
                    .apply {
                        duration = 300
                        interpolator = FastOutLinearInInterpolator()
                        start()
                }

                val startYPrice = 0f - binding.activityPriceTv.height
                val endYPrice = binding.activityPriceTv.y

                ObjectAnimator.ofFloat(
                    binding.activityPriceTv,
                    View.Y,
                    startYPrice,
                    endYPrice)
                    .apply {
                        duration = 300
                        interpolator = FastOutLinearInInterpolator()
                        start()
                    }

                val startXAccessibility = -deviceWidth.toFloat()
                val endXAccessibility = binding.activityAccessibilityTv.x

                ObjectAnimator.ofFloat(
                    binding.activityAccessibilityTv,
                    View.X,
                    startXAccessibility,
                    endXAccessibility)
                    .apply {
                        duration = 300
                        interpolator = FastOutLinearInInterpolator()
                        start()
                    }

                val startXParticipants = deviceWidth.toFloat()
                val endXParticipants = binding.activityParticipantsTv.x

                ObjectAnimator.ofFloat(
                    binding.activityParticipantsTv,
                    View.X,
                    startXParticipants,
                    endXParticipants)
                    .apply {
                        duration = 300
                        interpolator = FastOutLinearInInterpolator()
                        start()
                    }

                binding.activityPriceTv.visibility = View.VISIBLE
                binding.activityDisplayTv.visibility = View.VISIBLE
                binding.activityAccessibilityTv.visibility = View.VISIBLE
                binding.activityParticipantsTv.visibility = View.VISIBLE
            }

            override fun onTransitionResume(transition: Transition) {
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionCancel(transition: Transition) {
            }

            override fun onTransitionStart(transition: Transition) {
            }

        })

        return binding.root
    }

    fun returnAnimations() {

        val startYActivityDisplay = binding.activityDisplayTv.y.toFloat()
        val endYActivityDisplay = deviceHeight.toFloat()

        ObjectAnimator.ofFloat(
            binding.activityDisplayTv,
            View.Y,
            startYActivityDisplay,
            endYActivityDisplay)
            .apply {
                duration = 250
                interpolator = FastOutLinearInInterpolator()
                start()
            }.doOnEnd {
                navigateToRandomActivityUnexpandedFragment()
            }

        val startYPrice = binding.activityPriceTv.y
        val endYPrice = 0f - binding.activityPriceTv.height

        ObjectAnimator.ofFloat(
            binding.activityPriceTv,
            View.Y,
            startYPrice,
            endYPrice)
            .apply {
                duration = 250
                interpolator = FastOutLinearInInterpolator()
                start()
            }

        val startXAccessibility = binding.activityAccessibilityTv.x
        val endXAccessibility = -deviceWidth.toFloat()

        ObjectAnimator.ofFloat(
            binding.activityAccessibilityTv,
            View.X,
            startXAccessibility,
            endXAccessibility)
            .apply {
                duration = 250
                interpolator = FastOutLinearInInterpolator()
                start()
            }

        val startXParticipants = binding.activityParticipantsTv.x
        val endXParticipants = deviceWidth.toFloat()

        ObjectAnimator.ofFloat(
            binding.activityParticipantsTv,
            View.X,
            startXParticipants,
            endXParticipants)
            .apply {
                duration = 250
                interpolator = FastOutLinearInInterpolator()
                start()
            }


        binding.activityPriceTv.visibility = View.VISIBLE
        binding.activityDisplayTv.visibility = View.VISIBLE
        binding.activityAccessibilityTv.visibility = View.VISIBLE
        binding.activityParticipantsTv.visibility = View.VISIBLE
    }

    fun unpackParcel(){
        var activityResponse = args.myArgument
        if(activityResponse == null)
            return

        binding.activityDisplayTv.text = activityResponse.activity

        if(activityResponse.price != null){
            if(activityResponse.price!! > 0){
                binding.activityPriceTv.text = "Price: " + Math.round((activityResponse.price!! * 100)) + "$"
            }

            else {
                binding.activityPriceTv.text = "Price: free"
            }
        }

        var accessibilityString = ""

        if(activityResponse.accessibility != null){

            if(activityResponse.accessibility!! < 0.33)
                accessibilityString = "No Effort"
            if(activityResponse.accessibility!! > 0.33 && activityResponse.accessibility!! < 0.66f)
                accessibilityString = "Slight effort"

            if(activityResponse.accessibility!! > 0.66)
                accessibilityString = "Tiring but rewarding"
        }

        binding.activityAccessibilityTv.text = "Accessibility: " + accessibilityString

        binding.activityParticipantsTv.text = "Participants: " + activityResponse.participants
    }

    fun navigateToRandomActivityUnexpandedFragment(){
        val directions = RandomActivityExpandedDirections.
        actionRandomActivityExpandedToRandomActivityFragment()

        val transitionFrom = binding.getRandomThoughtIv
        val transitionTo = getString(R.string.unexpanded_activity_transition_name)
        val extras = FragmentNavigatorExtras(transitionFrom to transitionTo)
        findNavController().navigate(directions, extras)
    }
}