package com.m68476521.mike.journaler

import android.location.Location
import android.location.LocationListener
import android.location.LocationProvider
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import com.m68476521.mike.journaler.database.Note
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity: ItemActivity() {
    private var note: Note? = null
    private var location: Location? = null

    override fun getActivityTitle() = R.string.app_name
    override val tag = "Note activity"
    override fun getLayout() = R.layout.activity_note

    private var textWatcher = object: TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            updateNote()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    private val locationListener = object: LocationListener {
        override fun onLocationChanged(p0: Location?) {
            p0?.let {
                LocationProvider.unsubscribe(this)
                location = p0
                val title = getNoteTitle()
                val content = getNoteContent()
                note = Note(title, content, p0)
                val task = object : AsyncTask<Note, Void, Boolean>() {
                    override fun doInBackground(vararg p0: Note?): Boolean {
                        if (!p0.isEmpty()) {
                            val param = params[0]
                            param?.let {
                                return Db.inster(param)
                            }
                        }
                        return false
                    }

                    override fun onPostExecute(result: Boolean) {
                        result?.let {
                            if(result) {
                                Log.i(tag, "note inserted")
                            } else {
                                Log.e(tag, "note not inserted")
                            }
                        }
                    }
                }
                task.execute(note)
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
            val task = object : AsyncTask<Note, Void, Boolean>() {
                override fun doInBackground(vararg p0: Note?): Boolean {
                    if (!p0.isEmpty()) {
                        val param = p0[0]
                        param?.let {
                            return Db.update(p0)
                        }
                    }
                    return false
                }

                override fun onPostExecute(result: Boolean?) {
                    result?.let {
                        if (result) {
                            Log.i(tag, "note update")
                        } else {
                            Log.e(tag, "note not update")
                        }
                    }
                }
            }
            task.execute(note)
        }
    }

    fun getNoteContent(): String {
        return note_content.text.toString()
    }

    fun getNoteTitle(): String {
        return note_title.text.toString()
    }

}