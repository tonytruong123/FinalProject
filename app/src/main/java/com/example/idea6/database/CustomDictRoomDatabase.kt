package com.example.idea6.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope

@Database(entities = [CustomDict::class], version = 2, exportSchema = false)
abstract class CustomDictRoomDatabase : RoomDatabase() {

    abstract fun customDictDao(): CustomDictDao

    companion object {
        @Volatile
        private var INSTANCE: CustomDictRoomDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): CustomDictRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CustomDictRoomDatabase::class.java,
                    "custom_word_database"
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
