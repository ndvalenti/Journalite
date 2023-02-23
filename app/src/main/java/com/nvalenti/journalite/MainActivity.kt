package com.nvalenti.journalite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nvalenti.journalite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // TODO: Can this be moved to a fragment? Problems with AppCompatActivity and setupActionBarWithNavController, move what we can
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navigationBar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigationHost) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener {_, destination, _ ->
            when(destination.id) {
                R.id.navigation_lockscreen -> {
                    navView.visibility = View.GONE
                }
                else -> {
                    navView.visibility = View.VISIBLE
                }
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_today, R.id.navigation_journal, R.id.navigation_items, R.id.navigation_lockscreen
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        observeAuthenticationState()
    }

    private fun observeAuthenticationState() {
        viewModel.lockState.observe(this, Observer { lockState: MainViewModel.LockState ->
            when (lockState) {
                MainViewModel.LockState.LOCKED -> {
                    navController.popBackStack()
                    navController.navigate(R.id.navigation_lockscreen)
                }
                MainViewModel.LockState.UNREGISTERED -> {
                    navController.navigate(R.id.navigation_register)
                }
                else -> {
                    navController.popBackStack()
                    navController.navigate(R.id.main_navigation)
                }
            }
        })
    }
}