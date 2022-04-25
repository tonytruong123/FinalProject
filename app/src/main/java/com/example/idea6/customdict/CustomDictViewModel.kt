package com.example.idea6.customdict

import androidx.lifecycle.*
import com.example.idea6.database.CustomDict
import kotlinx.coroutines.launch

class CustomDictViewModel(private val repository: CustomDictRepository) : ViewModel() {
    val allWords: LiveData<List<CustomDict>> = repository.allWords.asLiveData()
    fun insert(word: CustomDict) = viewModelScope.launch {
        repository.insert(word)
    }
    fun delete(name: String) = viewModelScope.launch {
        repository.delete(name)
    }
    fun wordIsPresent(name: String): Boolean {
        return repository.wordIsPresent(name)
    }
}

class CustomDictViewModelFactory(private val repository: CustomDictRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomDictViewModel::class.java)) {
            return CustomDictViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}