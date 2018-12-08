package com.m68476521.mike.journaler.activity

import android.databinding.DataBindingUtil
import android.location.Location
import android.os.Bundle
import com.m68476521.mike.journaler.BaseActivity
import com.m68476521.mike.journaler.R
import com.m68476521.mike.journaler.model.Note

abstract class BindingActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: check why this binding is not working with activity_binding.xml
//        val binding : ActivityBindingBinding = DataBindingUtil.setContentView(this, R.layout.activity_binding)
//        val location = Location("dummy")
//        val note = Note("my note", "bla", location)
//        binding.note = note
    }
}