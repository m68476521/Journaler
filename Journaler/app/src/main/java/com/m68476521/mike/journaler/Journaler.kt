package com.m68476521.mike.journaler

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import com.m68476521.mike.journaler.receiver.NetworkReceiver
import com.m68476521.mike.journaler.service.MainService

class Journaler : Application() {
    private val networkReceiver = NetworkReceiver()
    companion object {
        val tag = "Journaler"
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Log.v(tag, "[ ON CREATE ]")
        //TODO: This method is Deprecated change later
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.w(tag, " [ ON LOW MEMORY ]")
        stopService()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.d(tag, " [ ON TRIM MEMORY ]: $level")
    }

    private fun stopService() {
        val serviceIntent = Intent(this, MainService::class.java)
        stopService(serviceIntent)
    }
}