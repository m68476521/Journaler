package com.m68476521.mike.journaler

enum class MODE(val mode: Int) {
    CREATE(0),
    EDIT(1),
    VIEW(2);

    companion object {
        const val EXTRAS_KEY = "MODE"

        fun getByValue(value: Int): MODE {
            values().forEach {
                item ->
                if (item.mode == value)
                    return item
            }
            return VIEW
        }
    }
}