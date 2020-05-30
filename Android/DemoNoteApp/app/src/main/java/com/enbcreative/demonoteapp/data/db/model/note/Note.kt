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
    var published: Boolean? = true,
    var crudOperation: Int? = INSERT
) {
    companion object {
        const val INSERT = 0
        const val DELETE = 1
        const val UPDATE = 2
    }
}