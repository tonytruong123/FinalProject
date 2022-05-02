package com.example.idea6

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class NewSettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var activity:MainActivity

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        activity = getActivity() as MainActivity
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        // Set up a listener whenever a key changes
        preferenceScreen.sharedPreferences
            ?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        // Unregister the listener whenever a key changes
        preferenceScreen.sharedPreferences
            ?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            "theme_color" -> activity?.recreate()
        }
        activity.createTimedNotif()
    }
}