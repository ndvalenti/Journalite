package com.nvalenti.journalite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nvalenti.journalite.controller.NotificationWorker
import com.nvalenti.journalite.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            (application as JournalApplication).database.journalDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navigationBar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigationHost) as NavHostFragment

        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_today, R.id.navigation_journal, R.id.navigation_items, R.id.navigation_item_detail
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        observeNavBarVisibility(navView)

        val itemBadge = navView.getOrCreateBadge(R.id.navigation_today)

        val badgeObserver = Observer<Int> { items ->
            if (items > 0) {
                itemBadge.isVisible = true
                itemBadge.number = items
            } else {
                itemBadge.isVisible = false
            }
        }
        viewModel.waitingItems.observe(this, badgeObserver)
        viewModel.createWorkRequest = { message, timeDelayInMinutes ->
            createWorkRequest(message, timeDelayInMinutes)
        }
    }

    private fun createWorkRequest(message: String, timeDelayInMinutes: Long) {
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(timeDelayInMinutes, TimeUnit.MINUTES)
            .setInputData(workDataOf(
                "title" to "Journalite",
                "message" to message
            ))
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun observeNavBarVisibility(navView: BottomNavigationView) {
        MainViewModel.navBarVisible.observe(this) { visible ->
            if (visible) {
                navView.visibility = View.VISIBLE
            } else {
                navView.visibility = View.GONE
            }
        }
    }
}