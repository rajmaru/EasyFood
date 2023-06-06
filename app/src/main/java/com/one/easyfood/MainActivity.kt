// MainActivity.kt

package com.one.easyfood

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.one.easyfood.databinding.ActivityMainBinding
import com.one.easyfood.networkconnection.NetworkConnection

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var networkConnection: NetworkConnection
    private var isConnected: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
        checkInternetConnection()
    }

    override fun onStop() {
        super.onStop()
        networkConnection.unregisterNetworkCallback()
    }

    private fun checkInternetConnection() {
        networkConnection = NetworkConnection(this)
        networkConnection.observe(this) { isConnected ->
            this.isConnected = isConnected
        }
    }

    private fun init() {
        // Bottom Navigation
        val navController = Navigation.findNavController(this, R.id.host_fragment)
        NavigationUI.setupWithNavController(binding.btmNav, navController)
    }

    private fun onClick() {
        binding.mainSearchBtn.setOnClickListener {
            if (isConnected) {
                startActivity(Intent(this, SearchActivity::class.java))
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
