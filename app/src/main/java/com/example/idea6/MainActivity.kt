package com.example.idea6

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.RelativeLayout
import android.util.Log.d
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import androidx.work.*
import com.example.idea6.customdict.CustomDictViewModel
import com.example.idea6.customdict.CustomDictViewModelFactory
import com.example.idea6.databinding.ActivityMainBinding
import com.example.idea6.worker.RefreshWorkWorker
import com.example.idea6.worker.TabPageAdapter
import com.google.android.material.tabs.TabLayout
import java.io.File
import java.util.concurrent.TimeUnit
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    var globalVar = 0

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
        private const val LIGHT = R.style.Theme_Material3_Light
        private const val DARK = R.style.Theme_Material3_Dark
        private const val SYSTEM_DEFAULT = R.style.Theme_Material3_DayNight
        const val CHANNEL_ID = "new_word_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getThemeFromSettings()
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme()
        setUpTabBar()
        setBackground()
        val relativeLayout1 = findViewById<RelativeLayout>(R.id.relativeLayout1)
        if (globalVar == 0){ relativeLayout1.setBackgroundResource(R.color.white)}
        if (globalVar == 1){ relativeLayout1.setBackgroundResource(R.color.scuff)}

        val check = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notification", true)

        val dir = getFilesDir()

        copyFile("dictionary_edit.xls")
        createNotificationChannel()
        createTimedNotif()
    }

    private fun setUpTabBar()
    {
        val tabs = findViewById<TabLayout>(R.id.tabLayout)
        val adapter = TabPageAdapter(this, tabs.tabCount)
        val  viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                tabs.selectTab(tabs.getTabAt(position))
            }
        })

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab)
            {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    public fun getDatabase(): CustomDictViewModel {
        return customDictViewModel
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
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

    private fun getThemeFromSettings() {
        when (PreferenceManager.getDefaultSharedPreferences(this).getString(KEY_THEME, "Light")) {
            "Light" -> currentTheme = LIGHT
            "Dark" -> currentTheme = DARK
            "System Default" -> currentTheme = SYSTEM_DEFAULT
        }
    }
    private fun setBackground() {
        when (PreferenceManager.getDefaultSharedPreferences(this).getString(KEY_THEME, "Light")) {
            "Light" -> globalVar = 0
            "Dark" -> globalVar = 1
        }
    }

    public fun createTimedNotif(){
        val hour = PreferenceManager.getDefaultSharedPreferences(this).getString("notification_hour", "8")!!.toInt()
        val minute = PreferenceManager.getDefaultSharedPreferences(this).getString("notification_minute", "8")!!.toInt()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formatted = current.format(formatter)
        //d("testtime", hour.toString())
        //d("testtime", minute.toString())
        //d("testtime", formatted.toString())
        val cur_time = formatted.split(":").toTypedArray()
        var timediff = ((hour - cur_time[0].toInt()) * 3600) + ((minute - cur_time[1].toInt()) * 60) - cur_time[2].toInt()
        //d("testtime", timediff.toString())

        if(timediff < 0){
            timediff += 86400
        }
        d("testtime", timediff.toString())

        val imageWorker = OneTimeWorkRequestBuilder<RefreshWorkWorker>()
            .setInitialDelay(timediff.toLong(), TimeUnit.SECONDS)
            .addTag("Refresh Word Save")
            .build()
        // 2
        workManager.enqueueUniqueWork(
            "Timed-Notif",
            ExistingWorkPolicy.REPLACE,
            imageWorker
        )
    }

    //For testing notification
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