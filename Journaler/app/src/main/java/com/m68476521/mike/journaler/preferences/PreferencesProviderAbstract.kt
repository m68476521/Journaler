package com.m68476521.mike.journaler.preferences

import android.content.Context
import android.content.SharedPreferences

abstract class PreferencesProviderAbstract {
    abstract fun obtain(configuration: PreferencesConfiguration, context: Context): SharedPreferences
}