package com.m68476521.mike.journaler.model

import android.location.Location
import com.m68476521.mike.journaler.database.DbModel

abstract class Entry(var title: String, var message: String,var location: Location) : DbModel()