package com.enbcreative.demonoteapp.data.repository

import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.user.User
import com.enbcreative.demonoteapp.data.network.ApiResponse
import com.enbcreative.demonoteapp.data.network.SafeApiRequest
import com.enbcreative.demonoteapp.data.network.WebApi
import com.enbcreative.demonoteapp.utils.Coroutines

class UserRepository(
    private val api: WebApi,
    private val db: AppDatabase
) : SafeApiRequest() {
    suspend fun login(email: String, password: String): ApiResponse =
        apiRequest { api.login(email, password) }

    suspend fun signUp(name: String, email: String, password: String): ApiResponse =
        apiRequest { api.signup(name, email, password) }

    fun saveUser(user: User) = Coroutines.io { db.getUserDao().upsert(user) }
    fun getUser() = Coroutines.io { db.getUserDao().getUser() }
}