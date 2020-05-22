package com.enbcreative.demonoteapp.data.repository

import com.enbcreative.demonoteapp.data.network.ApiResponse
import com.enbcreative.demonoteapp.data.network.SafeApiRequest
import com.enbcreative.demonoteapp.data.network.WebApi

class UserRepository(private val api: WebApi) : SafeApiRequest() {
    suspend fun login(email: String, password: String): ApiResponse {
        return apiRequest { api.login(email, password) }
    }
}