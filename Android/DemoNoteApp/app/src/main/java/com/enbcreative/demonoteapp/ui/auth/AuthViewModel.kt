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
import com.enbcreative.demonoteapp.utils.logd

class AuthViewModel(private val repository: UserRepository) : ViewModel() {
    var email: String? = null
    var password: String? = null
    var listener: ProcessListener? = null

    private val _loginResult = MutableLiveData<String>()

    fun onLoginButtonClick(v: View) {
        listener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            // Handle al errors here
            listener?.onFailure("Invalid Email or Password")
            return
        }

        // Success
        listener?.onSuccess()

        if (TEST_MODE.not()) {
            Coroutines.main {
                try {
                    val loginResponse = repository.login(email!!, password!!)
                    _loginResult.value =
                        loginResponse.user?.toString()
                            ?: "User not Found! - ${loginResponse.message}"
                } catch (e: ApiException) {
                    _loginResult.value = "Login failed with code: ${e.message}"
                }
            }
        } else {
            try {
                val result = LoginRepository(LoginDataSource()).login(email!!, password!!)
                if (result is Result.Success) _loginResult.value = result.data.toString()
                else _loginResult.value = "Login failed"
            } catch (e: Exception) {
                logd("Login is failed....")
                e.printStackTrace()
            }
        }

        listener?.onSuccessResult(_loginResult)
    }
}