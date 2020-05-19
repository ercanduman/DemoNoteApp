package com.enbcreative.demonoteapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModel provider factory to instantiate AuthViewModel.
 * Required given AuthViewModel has non-empty constructor.
 */
class AuthViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}