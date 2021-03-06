package com.m68476521.mike.journaler.navigation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import com.m68476521.mike.journaler.R

class NavigationDrawerAdapter(val ctx: Context, val items: List<NavigationDrawerItem>) : BaseAdapter() {
    private val tag = "Nav, drw, adpter"
    override fun getView(position: Int, v: View?, group: ViewGroup?): View {
        val inflater = LayoutInflater.from(ctx)
        var view = v
        if (view == null)
            view = inflater.inflate(R.layout.adapter_navigation_drawer, null) as LinearLayout

        val item = items[position]
        val title = view.findViewById<Button>(R.id.drawer_item)
        title.text = item.title
        title.setOnClickListener {
            if (item.enabled)
                item.onClick.run()
            else
                Log.w(tag, " Item is disable: $item")
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0L
    }

    override fun getCount(): Int {
        return items.size
    }
}
