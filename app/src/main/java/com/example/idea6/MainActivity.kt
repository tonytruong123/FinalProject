package com.example.idea6

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.idea6.customdict.CustomDict
import com.example.idea6.customdict.CustomDictRoomDatabase
import com.example.idea6.customdict.CustomDictViewModel
import com.example.idea6.customdict.CustomDictViewModelFactory
import com.example.idea6.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val customDictViewModel: CustomDictViewModel by viewModels {
        CustomDictViewModelFactory((application as CustomDictApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    public fun getDatabase(): CustomDictViewModel {
        return customDictViewModel
    }

}