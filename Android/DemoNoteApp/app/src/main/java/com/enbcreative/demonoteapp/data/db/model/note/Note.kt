package com.enbcreative.demonoteapp.data.db.model.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    var content: String,
    var date: String,
    val userId: Int? = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)