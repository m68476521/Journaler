package com.m68476521.mike.journaler

import android.os.Bundle
import android.util.Log

abstract class ItemActivity: BaseActivity() {
    protected var mode = MODE.VIEW

    override fun getActivityTitle() = R.string.app_name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val modeTest = intent.getIntExtra(MODE.EXTRAS_KEY, MODE.VIEW.mode)
        mode = MODE.getByValue(modeTest)
        Log.v(tag, "Mode [ $mode ]")
    }
}