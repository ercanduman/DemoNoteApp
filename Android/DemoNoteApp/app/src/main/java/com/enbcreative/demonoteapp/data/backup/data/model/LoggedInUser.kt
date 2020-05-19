package com.enbcreative.demonoteapp.data.backup.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String,
    val userEmail: String,
    val userPassword: String
)
