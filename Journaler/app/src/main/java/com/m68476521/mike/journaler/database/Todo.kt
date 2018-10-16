package com.m68476521.mike.journaler.database

import android.location.Location

data class Todo(var title: String, var message: String, var location: Location, var scheduler: Long) : DbModel() {
    override var id: Long = 0L
}