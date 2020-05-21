package com.enbcreative.demonoteapp.data.network

import com.enbcreative.demonoteapp.data.db.model.User

data class ApiResponse(
    val isSuccessful: Boolean?,
    val message: String?,
    val user: User?
)