package com.enbcreative.demonoteapp.data.db.model.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int? = 0,
    var content: String,
    var created_at: String
)