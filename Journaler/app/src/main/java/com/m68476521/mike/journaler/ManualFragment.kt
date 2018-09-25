package com.m68476521.mike.journaler
import android.os.Bundle

class ManualFragment: BaseFragment() {
    override val logTag = "Manual Fragment"
    override fun getLayout() = R.layout.fragment_manual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment = ItemsFragment()

        fragmentManager!!
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
    }
}