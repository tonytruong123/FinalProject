package com.example.idea6.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val _theme = MutableLiveData<String>("")
    val theme: LiveData<String> = _theme

    private val _textSize = MutableLiveData<String>("")
    val textSize: LiveData<String> = _textSize

    private val _notifyHour = MutableLiveData<Int>(0)
    val notifyHour: LiveData<Int> = _notifyHour

    private val _notifyMinute = MutableLiveData<Int>(0)
    val notifyMinute: LiveData<Int> = _notifyMinute

    fun setTheme(theme: String) {
        _theme.value = theme
    }

    fun setTextSize(textSize: String) {
        _textSize.value = textSize
    }

    fun setHour(hour: Int) {
        _notifyHour.value = hour
    }

    fun setMinute(minute: Int) {
        _notifyMinute.value = minute
    }

    fun resetSettings() {
        _theme.value = ""
        _textSize.value = ""
        _notifyHour.value = 0
        _notifyMinute.value = 0
    }
}