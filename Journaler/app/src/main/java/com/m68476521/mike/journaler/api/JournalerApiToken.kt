package com.m68476521.mike.journaler.api

import com.google.gson.annotations.SerializedName

data class JournalerApiToken(@SerializedName("id_token") val token: String, val experies: Long)