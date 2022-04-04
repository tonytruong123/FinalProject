package com.example.idea6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
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

        // Setup spinners for theme and text size
        val themeSpinner: Spinner = binding.themeSpinner
        val textSizeSpinner: Spinner = binding.textSizeSpinner

        // Create an ArrayAdapter using the string array and a default spinner layout
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.themes_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                themeSpinner.adapter = adapter
            }
            ArrayAdapter.createFromResource(
                it,
                R.array.textSize_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                textSizeSpinner.adapter = adapter
            }
        }

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