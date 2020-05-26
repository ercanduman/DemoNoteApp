package com.enbcreative.demonoteapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.data.network.SafeApiRequest
import com.enbcreative.demonoteapp.data.network.WebApi
import com.enbcreative.demonoteapp.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(
    private val db: AppDatabase,
    private val api: WebApi
) : SafeApiRequest() {
    private val noteList = MutableLiveData<List<Note>>()

    init {
        noteList.observeForever { saveNotes(it) }
    }

    fun saveNote(note: Note) = Coroutines.io { db.getNoteDao().insert(note) }
    fun updateNote(note: Note) = Coroutines.io { db.getNoteDao().update(note) }
    fun deleteNote(note: Note) = Coroutines.io { db.getNoteDao().delete(note) }
    suspend fun getAllNotes(): LiveData<List<Note>> {
        return withContext(Dispatchers.IO) {
            val userId = 1
            fetchNotes(userId)
            db.getNoteDao().getAllNotes()
        }
    }

    private suspend fun fetchNotes(userId: Int) {
        if (isFetchNeeded()) {
            val notes = apiRequest { api.getNotes(userId) }
            noteList.postValue(notes.notes)
        }
    }

    private fun isFetchNeeded(): Boolean {
        return true
    }

    private fun saveNotes(notes: List<Note>) = Coroutines.io { db.getNoteDao().saveAllNotes(notes) }
}