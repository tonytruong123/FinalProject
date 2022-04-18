package com.example.idea6.customdict

import androidx.annotation.WorkerThread
import com.example.idea6.data.Word
import kotlinx.coroutines.flow.Flow

class CustomDictRepository(private val customDictDao: CustomDictDao) {
    val allWords: Flow<List<CustomDict>> = customDictDao.getAlphabetizedWords()
    @WorkerThread
    suspend fun insert(word: CustomDict) {
        customDictDao.insert(word)
    }
    suspend fun delete(name: String) {
        customDictDao.delete(name)
    }
}