package com.m68476521.mike.journaler.service

import android.app.Service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.m68476521.mike.journaler.api.*
import com.m68476521.mike.journaler.database.Db
import com.m68476521.mike.journaler.execution.TaskExecutor
import com.m68476521.mike.journaler.model.Note
import com.m68476521.mike.journaler.model.Todo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            var headers = BackendServiceHeaderMap.obtain()
            val service = JournalerBackendService.obtain()
            val credentials = UserLoginRequest("username", "password")
            val tokenResponse = service
                    .login(headers, credentials)
                    .execute()
            if (tokenResponse.isSuccessful) {
                val token = tokenResponse.body()
                token?.let {
                    TokenManager.currentToken = token
                    headers = BackendServiceHeaderMap.obtain(true)
                    fetchNotes(service, headers)
                    fetchTodos(service, headers)
                }
            }
            Log.i(tag, "Synchronazing data [ END ]")
        }
    }

    private fun fetchNotes(service: JournalerBackendService, headers: Map<String, String>) {
        service
                .getNotes(headers)
                .enqueue(object : Callback<List<Note>> {
                    override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                        response?.let {
                            if (response.isSuccessful) {
                                val notes = response.body()
                                notes?.let { Db.insert(notes) }
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                        Log.e(tag, "It wasn't possible to fetch notes")
                    }
                })

    }

    private fun fetchTodos(service: JournalerBackendService, headers: Map<String, String>) {
        service.getTodos(headers)
                .enqueue(object : Callback<List<Todo>> {
                    override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
                        response?.let {
                            if (response.isSuccessful) {
                                val todos = response.body()
                                todos?.let {
                                    Db.insert(todos)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                        Log.e(tag, "It wasn't possible to fetch todos")
                    }
                })
    }
}