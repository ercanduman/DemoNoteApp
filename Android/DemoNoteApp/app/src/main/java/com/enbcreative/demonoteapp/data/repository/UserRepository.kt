package com.enbcreative.demonoteapp.data.repository

import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.user.User
import com.enbcreative.demonoteapp.data.network.ApiResponse
import com.enbcreative.demonoteapp.data.network.SafeApiRequest
import com.enbcreative.demonoteapp.data.network.WebApi

class UserRepository(
    private val api: WebApi,
    private val db: AppDatabase
) : SafeApiRequest() {
    suspend fun login(email: String, password: String): ApiResponse =
        apiRequest { api.login(email, password) }

    suspend fun signUp(name: String, email: String, password: String): ApiResponse =
        apiRequest { api.signup(name, email, password) }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)
    suspend fun getUser() = db.getUserDao().getUser()
}