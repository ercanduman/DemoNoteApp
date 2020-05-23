package com.enbcreative.demonoteapp.data.db.model.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val content: String,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)