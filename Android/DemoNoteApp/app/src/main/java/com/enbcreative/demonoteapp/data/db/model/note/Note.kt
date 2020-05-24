package com.enbcreative.demonoteapp.data.db.model.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    var content: String,
    var date: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)