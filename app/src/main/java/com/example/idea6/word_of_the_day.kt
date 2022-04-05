package com.example.idea6

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.example.idea6.databinding.FragmentWordOfTheDayBinding

class word_of_the_day : Fragment(R.layout.fragment_word_of_the_day) {

    private var _binding: FragmentWordOfTheDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWordOfTheDayBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val custom_dict_button = binding.goToDictButton
        val settings_button = binding.goToSettings
        custom_dict_button.setOnClickListener{
            val action = word_of_the_dayDirections.actionWordOfTheDayToCustomDictFragment()
            view.findNavController().navigate(action)
        }
        settings_button.setOnClickListener{
            val action = word_of_the_dayDirections.actionWordOfTheDayToSettingsFragment()
            view.findNavController().navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}