package com.enbcreative.demonoteapp.ui.auth

import android.view.View
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    var email: String? = null
    var password: String? = null
    var listener: ProcessListener? = null

    fun onLoginButtonClick(v: View) {
        listener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            // Handle al errors here
            listener?.onFailure("Invalid Email or Password")
            return
        }

        // Success
        listener?.onSuccess()
    }
}