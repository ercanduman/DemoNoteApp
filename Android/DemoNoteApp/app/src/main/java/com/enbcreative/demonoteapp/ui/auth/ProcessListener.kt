package com.enbcreative.demonoteapp.ui.auth

import androidx.lifecycle.LiveData

interface ProcessListener {
    fun onStarted()
    fun onSuccess()
    fun onSuccessResult(result: LiveData<String>)
    fun onFailure(message: String)
}