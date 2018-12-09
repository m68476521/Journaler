package com.journaler

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.test.InstrumentationRegistry
import android.util.Log
import com.m68476521.mike.journaler.service.MainService
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class MainServiceTest {
    private var ctx: Context ? = null
    private val tag = "Main Service Test"

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.v(tag, "Service connected")
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.v(tag, "Service disconnected")
        }
    }

    @Before
    fun beforeMainService() {
        Log.v(tag, "Starting service")
        ctx = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun testMainService() {
        Log.v(tag, "Running")
        assertNotNull(ctx)
        val serviceIntent = Intent(ctx, MainService::class.java)
        ctx?.startService(serviceIntent)
        val result = ctx?.bindService(serviceIntent, serviceConnection,
                android.content.Context.BIND_AUTO_CREATE)
        assert(result != null && result)
    }

    @After
    fun afterMainServiceTest() {
        Log.v(tag, "Finishing")
        ctx?.unbindService(serviceConnection)
        val serviceIntent = Intent(ctx, MainService::class.java)
        ctx?.stopService(serviceIntent)
    }

}