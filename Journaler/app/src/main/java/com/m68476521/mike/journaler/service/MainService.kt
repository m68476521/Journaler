package com.m68476521.mike.journaler.service

import android.app.Service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.m68476521.mike.journaler.execution.TaskExecutor

class MainService : Service(), DataSynchronization {
    private val tag = "Main Service"
    private var binder = getServiceBinder()
    private var executor = TaskExecutor.getInstance(1)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.v(tag, " [ ON CREATE ]")
    }

    override fun onBind(p0: Intent?): IBinder {
        Log.v(tag, "[ ON BIND ]")
        return binder
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.w(tag, " [ ON LOW MEMORY ] ")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        val result = super.onUnbind(intent)
        Log.v(tag, "[ UNBIND ] ")
        return result
    }

    private fun getServiceBinder(): MainServiceBinder = MainServiceBinder()

    inner class MainServiceBinder : Binder() {
        fun getService(): MainService = this@MainService
    }

    override fun onDestroy() {
        synchronize()
        super.onDestroy()
        Log.v(tag, " [ ON DESTROY ]")
    }

    override fun synchronize() {
        executor.execute {
            Log.i(tag, "Synchronizing data [ START ]")
            Thread.sleep(3000)
            Log.i(tag, "Synchronazing data [ END ]")
        }
    }
}