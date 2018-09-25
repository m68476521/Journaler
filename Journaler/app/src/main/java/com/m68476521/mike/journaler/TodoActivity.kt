package com.m68476521.mike.journaler

class TodoActivity: ItemActivity() {
    override fun getActivityTitle() = R.string.app_name
    override val tag = "Todo activity"
    override fun getLayout() = R.layout.activity_todo
}