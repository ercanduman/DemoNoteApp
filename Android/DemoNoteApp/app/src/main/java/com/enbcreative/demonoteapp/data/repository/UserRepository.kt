package com.enbcreative.demonoteapp.data.repository

import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.user.User
import com.enbcreative.demonoteapp.data.network.ApiResponse
import com.enbcreative.demonoteapp.data.network.SafeApiRequest
import com.enbcreative.demonoteapp.data.network.WebApi
import com.enbcreative.demonoteapp.data.prefs.Preferences
import com.enbcreative.demonoteapp.utils.Coroutines

class UserRepository(
    private val api: WebApi,
    private val db: AppDatabase,
    private val preferences: Preferences
) : SafeApiRequest() {
    suspend fun login(email: String, password: String): ApiResponse =
        apiRequest { api.login(email, password) }

    suspend fun signUp(name: String, email: String, password: String): ApiResponse =
        apiRequest { api.signup(name, email, password) }

    fun getUser() = Coroutines.io { db.getUserDao().getUser() }
    fun saveUser(user: User) = Coroutines.io {
        preferences.saveUserID(user.id)
        db.getUserDao().upsert(user)
    }
}