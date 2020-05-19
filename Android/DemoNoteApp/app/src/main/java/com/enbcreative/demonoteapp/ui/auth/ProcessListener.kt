package com.enbcreative.demonoteapp.ui.auth

interface ProcessListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}