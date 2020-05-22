package com.enbcreative.demonoteapp.data.repository

import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.User
import com.enbcreative.demonoteapp.data.network.ApiResponse
import com.enbcreative.demonoteapp.data.network.SafeApiRequest
import com.enbcreative.demonoteapp.data.network.WebApi

class UserRepository(
    private val api: WebApi,
    private val db: AppDatabase
) : SafeApiRequest() {
    suspend fun login(email: String, password: String): ApiResponse {
        return apiRequest { api.login(email, password) }
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)
}