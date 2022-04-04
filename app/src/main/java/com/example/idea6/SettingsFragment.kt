package com.example.idea6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.idea6.databinding.FragmentSettingsBinding
import com.example.idea6.model.SettingsViewModel

/**
 * [SettingsFragment] allows the user to configure a variety of settings for the app.
 */
class SettingsFragment : Fragment() {

    // Setup view binding for use in fragment
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    // Allow access to SettingsViewModel
    private val SettingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {  }
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}