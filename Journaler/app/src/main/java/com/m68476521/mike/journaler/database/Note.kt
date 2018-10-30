package com.m68476521.mike.journaler.database

import android.location.Location

data class Note(var title: String, var message: String, var location: Location) : DbModel() {
    override var id = 0L

}