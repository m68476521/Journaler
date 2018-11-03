package com.m68476521.mike.journaler.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesProvider : PreferencesProviderAbstract() {
    override fun obtain(configuration: PreferencesConfiguration, context: Context): SharedPreferences {
        return context.getSharedPreferences(configuration.key, configuration.mode)
    }
}