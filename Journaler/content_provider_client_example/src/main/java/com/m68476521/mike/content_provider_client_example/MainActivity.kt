package com.m68476521.mike.content_provider_client_example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        select.setOnClickListener {
        }
        insert.setOnClickListener {
        }
        update.setOnClickListener {
        }
        delete.setOnClickListener {
        }
    }
}
