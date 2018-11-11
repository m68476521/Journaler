package com.m68476521.mike.journaler

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ListView
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
            animate(button)
            val items = arrayOf(
                    getString(R.string.todos),
                    getString(R.string.notes)
            )

            val builder = AlertDialog.Builder(this@ItemsFragment.context)
                    .setTitle(R.string.choose_a_type)
                    .setCancelable(true)
                    .setOnCancelListener { animate(button, false) }
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
        val color = ContextCompat.getColor(context!!, R.color.vermilion_dark)
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

    private fun animate(button: FloatingActionButton, expand: Boolean = true) {
       val animation1 = ObjectAnimator.ofFloat(button, "scaleX",
               if (expand) { 1.5f } else { 1.0f})
        animation1.duration = 2000
        animation1.interpolator = BounceInterpolator()

        val animation2 = ObjectAnimator.ofFloat(button, "scaleY",
                if (expand) { 1.5f} else { 1.0f})
        animation2.duration = 2000
        animation2.interpolator = BounceInterpolator()

        val animation3 = ObjectAnimator.ofFloat(button, "alpha",
                if (expand) { 0.3f} else { 1.0f })
        animation3.duration = 500
        animation3.interpolator = AccelerateInterpolator()

        val set = AnimatorSet()
        set.play(animation1).with(animation2).before(animation3)
        set.start()
    }

    override fun onResume() {
        super.onResume()
        val btn = view?.findViewById<FloatingActionButton>(R.id.new_item)
        btn?.let { animate(btn, false) }

        val items = view?.findViewById<ListView>(R.id.items)
        items?.let {
            items.postDelayed({
                if (!activity!!.isFinishing) {
                    items.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey_text_middle))
                }
            }, 3000)
        }
    }
}