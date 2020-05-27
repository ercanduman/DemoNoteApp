package com.enbcreative.demonoteapp.ui.auth

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enbcreative.demonoteapp.TEST_MODE
import com.enbcreative.demonoteapp.data.backup.data.LoginDataSource
import com.enbcreative.demonoteapp.data.backup.data.LoginRepository
import com.enbcreative.demonoteapp.data.backup.data.Result
import com.enbcreative.demonoteapp.data.repository.UserRepository
import com.enbcreative.demonoteapp.utils.ApiException
import com.enbcreative.demonoteapp.utils.Coroutines

class AuthViewModel(private val repository: UserRepository) : ViewModel() {
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var passwordConfirmed: String? = null

    var listener: ProcessListener? = null

    private val _loginResult = MutableLiveData<String>()
    fun getLoggedInUser() = repository.getUser()

    fun onLoginButtonClick(v: View) {
        listener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            // Handle al errors here
            listener?.onFailure("Invalid Email or Password")
            return
        }

        if (TEST_MODE.not()) {
            Coroutines.main {
                try {
                    val loginResponse = repository.login(email!!, password!!)
                    loginResponse.user?.let {
                        _loginResult.value = it.toString()
                        repository.saveUser(it)
                        listener?.onSuccess()
                        return@main
                    }
                    _loginResult.value = loginResponse.message
                } catch (e: Exception) {
                    _loginResult.value = "Login failed with code: ${e.message}"
                }
            }
        } else {
            try {
                val result = LoginRepository(LoginDataSource()).login(email!!, password!!)
                if (result is Result.Success) {
                    _loginResult.value = result.data.toString()
                    listener?.onSuccess()
                } else _loginResult.value = "Login failed"
            } catch (e: Exception) {
                _loginResult.value = "Login failed with code: ${e.message}"
            }
        }

        listener?.onSuccessResult(_loginResult)
    }

    fun onSignUpButtonClick(v: View) {
        listener?.onStarted()

        if (name.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || passwordConfirmed.isNullOrEmpty()) {
            listener?.onFailure("Invalid or Empty field.")
            return
        }

        Coroutines.main {
            try {
                val signUpResponse = repository.signUp(name!!, email!!, password!!)
                signUpResponse.user?.let {
                    _loginResult.value = it.toString()
                    repository.saveUser(it)
                    listener?.onSuccess()
                    return@main
                }
                _loginResult.value = "Cannot Sign Up! - ${signUpResponse.message}"
            } catch (e: ApiException) {
                _loginResult.value = "Sign Up failed with code: ${e.message}"
            }
        }
        listener?.onSuccessResult(_loginResult)
    }
}