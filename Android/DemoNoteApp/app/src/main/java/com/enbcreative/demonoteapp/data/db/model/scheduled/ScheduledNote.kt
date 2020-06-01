package com.enbcreative.demonoteapp.data.db.model.scheduled

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ScheduledNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    var content: String,
    var created_at: String
)