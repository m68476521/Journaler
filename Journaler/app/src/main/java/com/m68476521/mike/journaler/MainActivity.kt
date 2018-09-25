package com.m68476521.mike.journaler

import android.os.Bundle

class MainActivity : BaseActivity() {
    override val tag = "Main activity"

    override fun getActivityTitle() = R.string.app_name
    override fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment = ItemsFragment()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
    }
}
