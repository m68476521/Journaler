package com.m68476521.mike.journaler.execution

import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class TaskExecutor private constructor(corePoolSize: Int, maximimPoolSize: Int,
                                       workQueue: BlockingDeque<Runnable>?) : ThreadPoolExecutor(
        corePoolSize, maximimPoolSize, 0L, TimeUnit.MILLISECONDS, workQueue) {
    companion object {
        fun getInstance(capacity: Int): TaskExecutor {
            return TaskExecutor(capacity, capacity * 2, LinkedBlockingDeque<Runnable>())
        }
    }
}