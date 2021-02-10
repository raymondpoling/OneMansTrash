package org.mousehole.americanairline.onemanstrash.utils

import android.util.Log

object DebugLogger {
    val LOG_TAG = "TAG_X"
    fun debug(msg:String) = Log.d(LOG_TAG, msg)
    fun error(t:Throwable) = Log.e(LOG_TAG, t.localizedMessage, t)
}