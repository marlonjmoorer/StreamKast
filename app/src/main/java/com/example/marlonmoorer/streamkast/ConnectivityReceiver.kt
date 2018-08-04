package com.example.marlonmoorer.streamkast

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnectivityReceiver(val listener:ConnectivityReceiverListener):BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent?) {
       listener.onConnectionChanged(checkConnectionStatus(context))
    }

    private fun checkConnectionStatus(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}

interface ConnectivityReceiverListener {
    fun onConnectionChanged(isConnected: Boolean)
}