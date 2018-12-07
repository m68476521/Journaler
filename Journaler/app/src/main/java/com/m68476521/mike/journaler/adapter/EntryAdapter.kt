package com.m68476521.mike.journaler.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.m68476521.mike.journaler.R
import com.m68476521.mike.journaler.database.DbHelper

//TODO: Change this deprecated CursorAdapter
class EntryAdapter(context: Context, cursor: Cursor) : CursorAdapter(context, cursor) {
    @SuppressLint("InflateParams", "ViewHolder")
    override fun newView(p0: Context, p1: Cursor?, p2: ViewGroup?): View {
        val inflater = LayoutInflater.from(p0)
        return inflater.inflate(R.layout.adapter_entry, null)
    }

    override fun bindView(p0: View?, p1: Context?, p2: Cursor?) {
        p0?.let {
            val label = p0.findViewById<TextView>(R.id.title)
            label.text = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE))
        }
    }
}