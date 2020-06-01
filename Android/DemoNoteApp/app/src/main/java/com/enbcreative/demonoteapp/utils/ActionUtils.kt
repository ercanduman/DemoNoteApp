package com.enbcreative.demonoteapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.enbcreative.demonoteapp.FETCH_INTERVAL

fun isFetchNeeded(lastSavedTime: String): Boolean {
    val lastTime = lastSavedTime.toLong()
    val interval = System.currentTimeMillis().minus(lastTime)
    val minutes = (interval / (1000 * 60) % 24).toInt()
    return minutes >= FETCH_INTERVAL
}

fun isNetworkAvailable(context: Context): Boolean {
    val appContext = context.applicationContext
    var result = false
    val manager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        manager.apply {
            manager.getNetworkCapabilities(manager.activeNetwork)?.apply {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
    } else {
        @Suppress("DEPRECATION")
        manager.activeNetworkInfo?.apply {
            result =
                type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_MOBILE
        }
    }
    return result
}