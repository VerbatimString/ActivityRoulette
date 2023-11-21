package com.example.kotlinflowplayground.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlinflowplayground.R
import com.example.kotlinflowplayground.databinding.FragmentSavedActivitiesBinding

class SavedActivitiesFragment() : Fragment() {
    var _binding : FragmentSavedActivitiesBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }
}