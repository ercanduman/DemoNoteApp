package com.enbcreative.demonoteapp.data.db.model.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val userId: Int,
    var content: String,
    var created_at: String,
    var updated_at: String,
    var published: Boolean? = true
)