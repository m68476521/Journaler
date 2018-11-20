package com.m68476521.mike.journaler.receiver


import android.content.*
import android.net.ConnectivityManager
import android.os.IBinder
import android.util.Log
import com.m68476521.mike.journaler.service.MainService

private const val TAG = "Network-Receiver"

class NetworkReceiver : BroadcastReceiver() {

    private var service: MainService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            service == null
        }

        override fun onServiceConnected(p0: ComponentName?, binber: IBinder?) {
            if (binber is MainService.MainServiceBinder) {
                service = binber.getService()
                service?.synchronize()
            }
        }
    }

    override fun onReceive(context: Context?, p1: Intent?) {
        context?.let {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activityNetwork = cm.activeNetworkInfo
            val isConnected = activityNetwork != null && activityNetwork.isConnected
            if (isConnected) {
                Log.v(TAG, " Connectivity [ available]")
                if (service == null) {
                    val intent = Intent(context, MainService::class.java)
                    context.bindService(intent, serviceConnection, android.content.Context.BIND_AUTO_CREATE)
                } else {
                    service?.synchronize()
                }
            } else {
                Log.v(TAG, "Connectivity [ UNAVAILABLE ]")
                context.unbindService(serviceConnection)
            }
        }
    }
}