package com.example.mydiary.util

import android.view.View
import android.view.ViewConfiguration
import kotlinx.coroutines.*

open abstract class OnDoubleClickListener : View.OnClickListener {
    private var doubleClickTimeout = ViewConfiguration.getDoubleTapTimeout()
    private var firstClickTime: Long = 0L
    private lateinit var job: Job

    override fun onClick(v: View?) {
        val now = System.currentTimeMillis()
        if (now - firstClickTime < doubleClickTimeout) {
            job.cancel()
            firstClickTime = 0L;
            onDoubleClick(v);
        } else {
            firstClickTime = now;
            handleClick(v)
        }
    }

    abstract fun onSingleClick(v: View?)

    abstract fun onDoubleClick(v: View?)

    fun reset() {
        job.cancel()
    }

    private fun handleClick(v: View?) {
        job = CoroutineScope(Dispatchers.Main).launch {
            delay(doubleClickTimeout.toLong())
            onSingleClick(v);
            firstClickTime = 0L;
        }
    }
}