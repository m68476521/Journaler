package com.m68476521.mike.content_provider_client_example

import android.content.ContentValues
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private val gson = Gson()
    private val tag = "Main Activity Client"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        select.setOnClickListener {
            val task = object : AsyncTask<Unit, Unit, Unit>() {
                override fun doInBackground(vararg p0: Unit?) {
                    val selection = StringBuilder()
                    val selectionArgs = mutableListOf<String>()
                    val uri = Uri.parse("content://com.journaler.provider/notes")
                    val cursor = contentResolver.query(
                            uri, null, selection.toString(), selectionArgs.toTypedArray(), null
                    )
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
                        val titleIdx = cursor.getColumnIndexOrThrow("title")
                        val title = cursor.getString(titleIdx)
                        val messageIdx = cursor.getColumnIndexOrThrow("message")
                        val message = cursor.getString(messageIdx)
                        val locationIdx = cursor.getColumnIndexOrThrow("location")
                        val locationJson = cursor.getString(locationIdx)
                        val location = gson.fromJson<Location>(locationJson)
                        Log.v(tag, "Note retrieve via content provider [ $id, $title, $message, $location ]")

                    }
                    cursor.close()
                }
            }
            task.execute()
        }
        insert.setOnClickListener {
            val task = object : AsyncTask<Unit, Unit, Unit>() {
                override fun doInBackground(vararg p0: Unit?) {
                    for (x in 0..5) {
                        var uri = Uri.parse("content://com.journaler.provider/note")
                        val values = ContentValues()
                        values.put("title", "Title $x")
                        values.put("mesagge", "Message $x")
                        val location = Location("Stub location $x")
                        location.latitude = x.toDouble()
                        location.longitude = x.toDouble()
                        values.put("location", gson.toJson(location))
                        if (contentResolver.insert(uri, values) != null) {
                            Log.v(tag, "Note inserted [ $x ]")
                        } else {
                            Log.e(tag, "Note not inserted [ $x ]")
                        }
                    }
                }
            }
            task.execute()
        }
        update.setOnClickListener {
            val task = object : AsyncTask<Unit, Unit, Unit>() {
                override fun doInBackground(vararg p0: Unit?) {
                    val selection = StringBuilder()
                    val selectionArgs = mutableListOf<String>()
                    val uri = Uri.parse("content://com.journaler.provider/notes")
                    val cursor = contentResolver.query(uri, null, selection.toString(),
                            selectionArgs.toTypedArray(), null)

                    while (cursor.moveToNext()) {
                        val values = ContentValues()
                        val id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
                        val titleIdx = cursor.getColumnIndexOrThrow("title")
                        val title = "${cursor.getString(titleIdx)} upd: ${System.currentTimeMillis()}"
                        val messageIdx = cursor.getColumnIndexOrThrow("message")
                        val message = "${cursor.getString(messageIdx)} upd: ${System.currentTimeMillis()}"
                        val locationIdx = cursor.getColumnIndexOrThrow("location")
                        val locationJson = cursor.getString(locationIdx)
                        values.put("_id", id)
                        values.put("title", title)
                        values.put("message", message)
                        values.put("location", locationJson)

                        val updated = contentResolver.update(uri, values, "_id = ?", arrayOf(id.toString()))

                        if (updated > 0) {
                            Log.v(tag, "Notes updated [ $updated ]")
                        } else {
                            Log.e(tag, "Notes not updated")
                        }
                    }
                    cursor.close()
                }
            }
            task.execute()
        }
        delete.setOnClickListener {
        }
    }
}
