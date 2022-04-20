package com.example.idea6

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.work.*
import com.example.idea6.customdict.CustomDict
import com.example.idea6.customdict.CustomDictRoomDatabase
import com.example.idea6.customdict.CustomDictViewModel
import com.example.idea6.customdict.CustomDictViewModelFactory
import com.example.idea6.databinding.ActivityMainBinding
import com.example.idea6.worker.RefreshWorkWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val customDictViewModel: CustomDictViewModel by viewModels {
        CustomDictViewModelFactory((application as CustomDictApplication).repository)
    }
    private val workManager by lazy {
        WorkManager.getInstance(applicationContext)
    }

    private var currentTheme = LIGHT

    companion object {
        private const val KEY_THEME = "theme_color"
        private const val LIGHT = R.style.AppTheme_Light
        private const val DARK = R.style.AppTheme_Dark
        private const val HIGH_CONTRAST = R.style.AppTheme_HighContrast
        const val CHANNEL_ID = "new_word_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getThemeFromSettings()
        super.onCreate(savedInstanceState)
        setTheme()

        val dir = getFilesDir()

        copyFile("dictionary.xls")

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        customDictViewModel.insert(CustomDict("ac", "bc"))
//        customDictViewModel.delete("ac")

        // Get the navigation host fragment from this Activity
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Instantiate the navController using the NavHostFragment
        navController = navHostFragment.navController
        // Make sure actions in the ActionBar get propagated to the NavController
        //setupActionBarWithNavController(navController)
        createNotificationChannel()
        createPeriodicWorkRequest()
    }

    public fun getDatabase(): CustomDictViewModel {
        return customDictViewModel
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getThemeFromSettings() {
        when (PreferenceManager.getDefaultSharedPreferences(this).getString(KEY_THEME, "Light")) {
            "Light" -> currentTheme = LIGHT
            "Dark" -> currentTheme = DARK
            "High Contrast" -> currentTheme = HIGH_CONTRAST
        }
    }

    private fun createOneTimeWorkRequest() {
        // 1
        val imageWorker = OneTimeWorkRequestBuilder<RefreshWorkWorker>()
            .addTag("Refresh Word Save")
            .build()
        // 2
        workManager.enqueueUniqueWork(
            "oneTimeRefreshSave",
            ExistingWorkPolicy.KEEP,
            imageWorker
        )
    }

    private fun createPeriodicWorkRequest(){
        //15 Min Repeating
        val imageWorker = PeriodicWorkRequestBuilder<RefreshWorkWorker>(15, TimeUnit.MINUTES)
            .addTag("Refresh Word Save")
            .build()
        workManager.enqueueUniquePeriodicWork(
            "periodicRefeshSave",
            ExistingPeriodicWorkPolicy.KEEP,
            imageWorker
        )
    }

    private fun setTheme() {
        setTheme(currentTheme)
    }

    fun copyFile(filename: String) {
        this.assets.open(filename).use { stream ->
            File("${this.filesDir}/$filename").outputStream().use {
                stream.copyTo(it)
            }
        }
    }


}