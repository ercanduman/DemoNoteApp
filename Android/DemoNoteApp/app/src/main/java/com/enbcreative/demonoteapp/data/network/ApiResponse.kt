package com.enbcreative.demonoteapp.data.network

import com.enbcreative.demonoteapp.data.db.model.user.User
import org.intellij.lang.annotations.Language

/**
 * Should have JSON in below format
 */

@Language("JSON")
private const val JSON_RESPONSE_SUCCESS =
    "{\n  \"isSuccessful\": true,\n  \"message\": \"Login Successful\",\n  \"user\": {\n    \"id\": 1,\n    \"name\": \"Ercan Duman\",\n    \"email\": \"ercanduman30@gmail.com\",\n    \"email_verified_at\": null,\n    \"created_at\": \"2020.05.22 14:20:30\",\n    \"updated_at\": \"2020.05.22 14:20:30\"\n  }\n}"

@Language("JSON")
private const val JSON_RESPONSE_FAILED =
    "{\n  \"isSuccessful\": false,\n  \"message\": \"Login Failed. Invalid email or password!\"\n}"

data class ApiResponse(
    val isSuccessful: Boolean?,
    val message: String?,
    val user: User?
) {
    companion object {
        const val JSON_FIELD_USER = "user"
        const val JSON_FIELD_MESSAGE = "message"
        const val JSON_FIELD_IS_SUCCESSFUL = "isSuccessful"
    }
}