package com.nvalenti.journalite

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nvalenti.journalite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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
        //navView.setBackgroundColor(resources.getColor(R.color.journalite_primary, null))
        observeNavBarVisibility(navView)
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