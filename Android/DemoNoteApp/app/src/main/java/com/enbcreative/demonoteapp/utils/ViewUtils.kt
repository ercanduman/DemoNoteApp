package com.enbcreative.demonoteapp.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.enbcreative.demonoteapp.BuildConfig

fun Any.logd(message: String) {
    if (BuildConfig.DEBUG) Log.d(this.javaClass.simpleName, message)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}