package com.enbcreative.demonoteapp.data.db.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val created_at: String,
    val updated_at: String
) {
    /**
     * To store only one user (authenticated user) CURRENT_USER_ID will be used.
     */
    @PrimaryKey(autoGenerate = false)
    var userId = CURRENT_USER_ID
}