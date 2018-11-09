package com.m68476521.mike.journaler

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import com.m68476521.mike.journaler.database.Db
import com.m68476521.mike.journaler.execution.TaskExecutor
import com.m68476521.mike.journaler.location.LocationProvider
import com.m68476521.mike.journaler.model.Note
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : ItemActivity() {
    private var note: Note? = null
    private var location: Location? = null

    override fun getActivityTitle() = R.string.app_name
    override val tag = "Note activity"
    override fun getLayout() = R.layout.activity_note
    private val executor = TaskExecutor.getInstance(1)

    private var textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            updateNote()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(p0: Location?) {
            p0?.let {
                LocationProvider.unsubscribe(this)
                location = p0
                val title = getNoteTitle()
                val content = getNoteContent()
                note = Note(title, content, p0)
                executor.execute {
                    val param = note
                    var result = false
                    param?.let {
                        result = Db.insert(param)
                    }
                    if (result) {
                        Log.i(tag, "note inserted")
                    } else {
                        Log.e(tag, "note not inserted")
                    }
                }
            }
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        }

        override fun onProviderEnabled(p0: String?) {

        }

        override fun onProviderDisabled(p0: String?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note_title.addTextChangedListener(textWatcher)
        note_content.addTextChangedListener(textWatcher)
    }

    fun updateNote() {
        if (note != null && !TextUtils.isEmpty(getNoteTitle()) && !TextUtils.isEmpty(getNoteContent())) {
            LocationProvider.subscribe(locationListener)
        } else {
            note?.title = getNoteTitle()
            note?.message = getNoteContent()
            executor.execute {
                val param = note
                var result = true
                param?.let {
                    result = Db.update(param)
                }
                if (result) {
                    Log.i(tag, "Note updated.")
                } else {
                    Log.e(tag, "Note not updated.")
                }
            }
        }
    }

    fun getNoteContent(): String {
        return note_content.text.toString()
    }

    fun getNoteTitle(): String {
        return note_title.text.toString()
    }
}