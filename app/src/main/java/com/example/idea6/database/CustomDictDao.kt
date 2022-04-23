package com.example.idea6.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomDictDao {
    @Query("SELECT * FROM custom_table ORDER BY name ASC")
    fun getAlphabetizedWords(): Flow<List<CustomDict>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: CustomDict)

    @Query("DELETE FROM custom_table WHERE name = :del")
    suspend fun delete(del: String)
}