package com.enbcreative.demonoteapp.data.repository

import androidx.lifecycle.LiveData
import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.utils.Coroutines

class NoteRepository(private val db: AppDatabase) {
    fun saveNote(note: Note) = Coroutines.io { db.getNoteDao().insert(note) }
    fun updateNote(note: Note) = Coroutines.io { db.getNoteDao().update(note) }
    fun deleteNote(note: Note) = Coroutines.io { db.getNoteDao().delete(note) }
    fun getAllNotes(): LiveData<List<Note>> = db.getNoteDao().getAllNotes()
}