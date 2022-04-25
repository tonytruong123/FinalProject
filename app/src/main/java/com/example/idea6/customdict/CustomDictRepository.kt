package com.example.idea6.customdict

import androidx.annotation.WorkerThread
import com.example.idea6.database.CustomDict
import com.example.idea6.database.CustomDictDao
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
    fun wordIsPresent(name: String): Boolean {
        return customDictDao.containsPrimaryKey(name)
    }
}