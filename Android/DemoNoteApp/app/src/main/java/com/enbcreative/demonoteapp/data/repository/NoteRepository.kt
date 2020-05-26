package com.enbcreative.demonoteapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.data.network.SafeApiRequest
import com.enbcreative.demonoteapp.data.network.WebApi
import com.enbcreative.demonoteapp.utils.Coroutines
import com.enbcreative.demonoteapp.utils.isFetchNeeded
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
            val userId = 1 // TODO: Preferences.getUserID()
            fetchNotes(userId)
            db.getNoteDao().getAllNotes()
        }
    }

    private suspend fun fetchNotes(userId: Int) {
        // TODO: Preferences.getLastSavedTime()
        val currentTime = System.currentTimeMillis().toString()
        if (isFetchNeeded(currentTime)) {
            val notes = apiRequest { api.getNotes(userId) }
            noteList.postValue(notes.notes)
        }
    }

    private fun saveNotes(notes: List<Note>) = Coroutines.io { db.getNoteDao().saveAllNotes(notes) }
}