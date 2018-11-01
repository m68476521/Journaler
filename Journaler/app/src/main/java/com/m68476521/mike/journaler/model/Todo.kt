package com.m68476521.mike.journaler.model

import android.location.Location
import com.m68476521.mike.journaler.database.DbModel

class Todo(title: String, var message: String, var location: Location, var scheduledFor: Long) : DbModel() {
    override var id = 0L
}