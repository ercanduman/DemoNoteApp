package com.enbcreative.demonoteapp.data.repository

import android.content.Context
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.user.User
import com.enbcreative.demonoteapp.data.network.AuthResponse
import com.enbcreative.demonoteapp.data.network.SafeApiRequest
import com.enbcreative.demonoteapp.data.network.WebApi
import com.enbcreative.demonoteapp.data.prefs.Preferences
import com.enbcreative.demonoteapp.utils.Coroutines
import com.enbcreative.demonoteapp.utils.NoNetworkExceptions
import com.enbcreative.demonoteapp.utils.isNetworkAvailable

class UserRepository(
    private val api: WebApi,
    private val db: AppDatabase,
    private val preferences: Preferences,
    private val context: Context
) : SafeApiRequest() {
    suspend fun login(email: String, password: String): AuthResponse {
        if (isNetworkAvailable(context).not()) throw NoNetworkExceptions(context.getString(R.string.no_network))
        return apiRequest { api.login(email, password) }
    }

    suspend fun signUp(name: String, email: String, password: String): AuthResponse {
        if (isNetworkAvailable(context).not()) throw NoNetworkExceptions(context.getString(R.string.no_network))
        return apiRequest { api.signup(name, email, password) }
    }

    fun getUser() = Coroutines.io { db.getUserDao().getUser() }
    fun saveUser(user: User) = Coroutines.io {
        preferences.saveUserID(user.id)
        db.getUserDao().upsert(user)
    }
}