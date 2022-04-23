package com.example.idea6

import android.app.Application
import com.example.idea6.customdict.CustomDictRepository
import com.example.idea6.database.CustomDictRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CustomDictApplication : Application() {
    // Using by lazy so the database and the repository are
    // only created when they're needed
    // rather than when the application starts
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { CustomDictRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { CustomDictRepository(database.customDictDao()) }
}