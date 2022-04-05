package com.example.idea6

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.idea6.databinding.FragmentSettingsBinding
import com.example.idea6.model.SettingsViewModel

/**
 * [SettingsFragment] allows the user to configure a variety of settings for the app.
 */
class SettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    // Setup view binding for use in fragment
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    // Allow access to SettingsViewModel
    private val SettingsViewModel: SettingsViewModel by activityViewModels()

    // TimePickerDialog code provided by GeeksForGeeks; will be edited later for optimization
    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> // logic to properly handle
            // the picked timings by user
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        // Setup spinners
        val themeSpinner: Spinner = binding.themeSpinner
        val textSizeSpinner: Spinner = binding.textSizeSpinner
        val displayLimitSpinner: Spinner = binding.displayLimitSpinner

        val timeSelectButton: Button = binding.timeSelectButton

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
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                textSizeSpinner.adapter = adapter
            }
            ArrayAdapter.createFromResource(
                it,
                R.array.displayLimit_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                displayLimitSpinner.adapter = adapter
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            settingsViewModel = SettingsViewModel
            settingsFragment = this@SettingsFragment
        }
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * The following two methods are called when the user makes a selection from the Spinner objects.
     */
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0!!.id) {
            R.id.themeSpinner -> {
                SettingsViewModel.setTheme(p0.getItemAtPosition(p2).toString())
            }
            R.id.textSizeSpinner -> {
                SettingsViewModel.setTextSize(p0.getItemAtPosition(p2).toString())
            }
            R.id.displayLimitSpinner -> {
                SettingsViewModel.setDisplayLimit(p0.getItemAtPosition(p2).toString().toInt())
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    /**
     * Shows a [TimePickerDialog] to set the daily notification time.
     */
    fun showTimePickerDialog() {
        val timePicker = TimePickerDialog(
            requireContext(),
            timePickerDialogListener,
            12,
            10,
            false
        )
        timePicker.show()
    }
}