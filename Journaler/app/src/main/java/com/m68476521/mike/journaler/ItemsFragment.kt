package com.m68476521.mike.journaler

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ItemsFragment : BaseFragment() {
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
        intent.putExtra(MODE.EXTRAS_KEY, MODE.CREATE)
        startActivity(intent)
    }

    private fun openCreateTodo() {
        val intent = Intent(context, TodoActivity::class.java)
        intent.putExtra(MODE.EXTRAS_KEY, MODE.CREATE)
        startActivity(intent)
    }
}