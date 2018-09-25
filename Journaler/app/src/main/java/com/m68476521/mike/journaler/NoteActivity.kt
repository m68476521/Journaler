package com.m68476521.mike.journaler

class NoteActivity: ItemActivity() {
    override fun getActivityTitle() = R.string.app_name
    override val tag = "Note activity"
    override fun getLayout() = R.layout.activity_note
}