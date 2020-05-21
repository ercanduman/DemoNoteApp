package com.enbcreative.demonoteapp.data.repository

import com.enbcreative.demonoteapp.data.network.ApiResponse
import com.enbcreative.demonoteapp.data.network.WebApi
import retrofit2.Response

class UserRepository(private val api: WebApi) {
    suspend fun login(email: String, password: String): Response<ApiResponse> {
        return api.login(email, password)
    }
}