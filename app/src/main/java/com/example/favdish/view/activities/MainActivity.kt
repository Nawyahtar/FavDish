package com.example.favdish.view.activities

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.background.WorkNotification
import com.example.favdish.databinding.ActivityMainBinding
import com.example.favdish.model.database.FavDishRoomDatabase
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var executor: Executor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        doesWork()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setOnItemReselectedListener {

        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_dishes,
                R.id.navigation_favourite_dishes,
                R.id.navigation_random_dish
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.editDishFragment || nd.id == R.id.addUpdateFragment || nd.id == R.id.DetailDishFragment) {
                navView.visibility = View.GONE
            } else {
                navView.visibility = View.VISIBLE
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment_activity_main)
        return NavigationUI.navigateUp(navController, null)
    }

    private fun doesWork(){
        val workerRequest: PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(WorkNotification::class.java,8,TimeUnit.HOURS)
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "Log",
            ExistingPeriodicWorkPolicy.KEEP,
            workerRequest
        )
    }

}