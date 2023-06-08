package com.one.easyfood

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_FADE
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import com.one.easyfood.databinding.ActivityMainBinding
import com.one.easyfood.networkconnection.NetworkConnection

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var networkConnection: NetworkConnection
    private var isConnected: Boolean = false
    private lateinit var snackbar: Snackbar

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
            if (isConnected) {
                if(snackbar.isShown){
                    snackbar.dismiss()
                }
            } else {
                if(!snackbar.isShown){
                    snackbar.show()
                }
            }
        }
    }

    private fun init() {
        setupBottomNavigation()
        snackbar = Snackbar.make(binding.btmNav, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
            .setAnchorView(binding.btmNav)
            .setBackgroundTint(resources.getColor(R.color.snackbar_bg))
            .setTextColor(resources.getColor(R.color.snackbar_text))
            .setAnimationMode(ANIMATION_MODE_SLIDE)
            .setAction("Cancel"){
                snackbar.dismiss()
            }
            .setActionTextColor(resources.getColor(R.color.snackbar_text))
    }

    private fun setupBottomNavigation() {
        val navController = Navigation.findNavController(this, R.id.host_fragment)
        NavigationUI.setupWithNavController(binding.btmNav, navController)
    }

    private fun onClick() {
        binding.mainSearchBtn.setOnClickListener {
            if (isConnected) {
                startActivity(Intent(this, SearchActivity::class.java))
            }else{
                if(!snackbar.isShown){
                    snackbar.show()
                }
            }
        }
    }
}
