package com.enbcreative.demonoteapp.utils

import com.enbcreative.demonoteapp.FETCH_INTERVAL

fun isFetchNeeded(lastSavedTime: String): Boolean {
    val lastTime = lastSavedTime.toLong()
    val interval = System.currentTimeMillis().minus(lastTime)
    val minutes = (interval / (1000 * 60) % 24).toInt()
    return minutes >= FETCH_INTERVAL
}