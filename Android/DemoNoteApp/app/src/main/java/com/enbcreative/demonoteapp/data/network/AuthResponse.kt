package com.enbcreative.demonoteapp.data.network

import com.enbcreative.demonoteapp.data.db.model.user.User
import org.intellij.lang.annotations.Language

/**
 * Should have JSON in below format
 */

@Language("JSON")
private const val JSON_RESPONSE_SUCCESS =
    "{\n  \"error\": false,\n  \"message\": \"Login successful.\",\n  \"user\": {\n    \"id\": 2,\n    \"username\": \"test user\",\n    \"email\": \"test@mail.com\",\n    \"created_at\": \"2020-05-27\",\n    \"updated_at\": \"2020-05-27\",\n    \"gender\": \"male\"\n  }\n}"

@Language("JSON")
private const val JSON_RESPONSE_FAILED =
    "{\n  \"error\": true,\n  \"message\": \"Invalid email or password.\"\n}"

data class AuthResponse(
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