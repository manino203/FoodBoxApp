package com.example.foodboxapp.backend.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

interface NetworkAvailabilityChecker {
    fun isAvailable(): Boolean
}

class NetworkAvailabilityCheckerImpl(
    private val context: Context
): NetworkAvailabilityChecker{
    override fun isAvailable(): Boolean{
        return ((context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let { manager ->
                manager.activeNetwork?.let {
                    manager.getNetworkCapabilities(it)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                }
            }?: false).also {
                Log.d("network_availability", "$it")
        }
    }
}

