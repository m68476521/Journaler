package com.m68476521.mike.journaler.navigation

data class NavigationDrawerItem(val title: String, val onClick: Runnable, var enabled: Boolean = true)