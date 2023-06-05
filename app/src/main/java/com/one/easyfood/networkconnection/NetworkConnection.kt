// NetworkConnection.kt

package com.one.easyfood.networkconnection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import androidx.lifecycle.LiveData

class NetworkConnection(private val context: Context) : LiveData<Boolean>() {
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback
    private var isRegistered = false

    override fun onActive() {
        super.onActive()
        if (!isRegistered) {
            connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())
            isRegistered = true
        }
        updateNetworkConnection()
    }

    fun unregisterNetworkCallback() {
        if (isRegistered) {
            connectivityManager.unregisterNetworkCallback(networkConnectionCallback)
            isRegistered = false
        }
    }

    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        networkConnectionCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }
        }
        return networkConnectionCallback
    }

    private fun updateNetworkConnection() {
        val activeNetworkConnection: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetworkConnection?.isConnected == true)
    }
}
