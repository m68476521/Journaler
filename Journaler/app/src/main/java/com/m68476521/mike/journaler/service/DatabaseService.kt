package com.m68476521.mike.journaler.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.m68476521.mike.journaler.MODE
import com.m68476521.mike.journaler.database.Content
import com.m68476521.mike.journaler.database.Crud
import com.m68476521.mike.journaler.model.Note

class DatabaseService : IntentService("DatabaseService") {
    companion object {
        val EXTRA_ENTRY = "entry"
        val EXTRA_OPERATOR = "operation"
    }

    private val tag = "Database service"

    override fun onCreate() {
        super.onCreate()
        Log.v(tag, "[ ON CREATE ]")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.v(tag, "[ ON LOW MEMORY ]")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(tag, "[ ON DESTROY ]")
    }

    override fun onHandleIntent(p0: Intent?) {
        p0?.let {
            val note = p0.getParcelableExtra<Note>(EXTRA_ENTRY)
            note?.let {
                val operation = p0.getIntExtra(EXTRA_OPERATOR, -1)
                when (operation) {
                    MODE.CREATE.mode -> {
                        val result = Content.insert(note)
                        if (result)
                            Log.i(tag, "Note inserted")
                        else
                            Log.e(tag, "Note not inserted")
                        broadcastResult(result)
                    }
                    MODE.EDIT.mode -> {
                        val result = Content.update(note)
                        if (result)
                            Log.i(tag, "note updated")
                        else
                            Log.e(tag, "Note not updated")
                        broadcastResult(result)
                    }
                    else -> Log.w(tag, "Unknown mode [ $operation ]")
                }
            }
        }
    }

    private fun broadcastResult(result: Boolean) {
        val intent = Intent()
        intent.putExtra(Crud.BROADCAST_EXTRAS_KEY_CRUD_OPERATION_RESULT,
                if (result)
                    1
                else
                    0
        )
    }
}