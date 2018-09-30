package com.m68476521.mike.journaler

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.text.SimpleDateFormat
import java.util.*

class ItemsFragment : BaseFragment() {
    private val TODO_REQUEST = 1
    private val NOTE_REQUEST = 0
    override val logTag = "Items fragment"
    override fun getLayout() = R.layout.fragment_items

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(getLayout(), container, false)
        val button = view?.findViewById<FloatingActionButton>(R.id.new_item)

        button?.setOnClickListener {
            val items = arrayOf(
                    getString(R.string.todos),
                    getString(R.string.notes)
            )

            val builder = AlertDialog.Builder(this@ItemsFragment.context)
                    .setTitle(R.string.choose_a_type)
                    .setItems(items) { _, which ->
                        when (which) {
                            0 -> openCreateNote()
                            1 -> openCreateTodo()
                            else -> Log.v(logTag, "Unknown option selected [ $which ]")
                        }
                    }
            builder.show()
        }
        return view
    }

    private fun openCreateNote() {
        val intent = Intent(context, NoteActivity::class.java)
        val data = Bundle()
        data.putInt(MODE.EXTRAS_KEY, MODE.CREATE.mode)
        intent.putExtras(data)
        startActivityForResult(intent, NOTE_REQUEST)
    }

    private fun openCreateTodo() {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("MMM dd YYYY", Locale.ENGLISH)
        val timeFormat = SimpleDateFormat("HH:MM", Locale.ENGLISH)
        val intent = Intent(context, TodoActivity::class.java)
        val data = Bundle()
        data.putInt(MODE.EXTRAS_KEY, MODE.CREATE.mode)
        data.putString(TodoActivity.EXTRA_DATE, dateFormat.format(date))
        data.putString(TodoActivity.EXTRA_TIME, timeFormat.format(date))
        intent.putExtras(data)
        startActivityForResult(intent, TODO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TODO_REQUEST -> {
                if (resultCode == Activity.RESULT_OK)
                    Log.i(logTag, "Created a new TODO")
                else
                    Log.w(logTag, "Didn't created new TODO")
            }
            NOTE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK)
                    Log.i(logTag, "Created a new NOTE")
                else
                    Log.w(logTag, "Didn't created new NOTE")
            }
        }
    }
}