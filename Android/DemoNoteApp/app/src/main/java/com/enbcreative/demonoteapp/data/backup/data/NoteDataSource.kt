package com.enbcreative.demonoteapp.data.backup.data

import com.enbcreative.demonoteapp.data.db.model.note.Note

class NoteDataSource {
    fun getNotes(): List<Note> {
        val noteList = mutableListOf<Note>()
        for (i in 1..5) noteList.add(Note("Test Note Content - $i", "22.05.2020 1$i:10"))
        return noteList
    }
}