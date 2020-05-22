package com.enbcreative.demonoteapp.data.repository

import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.note.Note

class NoteRepository(private val db: AppDatabase) {
    suspend fun saveNote(note: Note) = db.getNoteDao().insert(note)
    suspend fun updateNote(note: Note) = db.getNoteDao().update(note)
    suspend fun deleteNote(note: Note) = db.getNoteDao().delete(note)
    suspend fun getNoteCount() = db.getNoteDao().getNoteCount()
    suspend fun getAllNotes() = db.getNoteDao().getAllNotes()
}