package com.enbcreative.demonoteapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enbcreative.demonoteapp.TEST_MODE
import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.data.network.SafeApiRequest
import com.enbcreative.demonoteapp.data.network.WebApi
import com.enbcreative.demonoteapp.data.prefs.Preferences
import com.enbcreative.demonoteapp.utils.Coroutines
import com.enbcreative.demonoteapp.utils.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(
    private val db: AppDatabase,
    private val api: WebApi,
    private val preferences: Preferences
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
            if (TEST_MODE.not())fetchNotes()
            db.getNoteDao().getAllNotes()
        }
    }

    private suspend fun fetchNotes() {
        val userId = preferences.getUserID()
        val currentTime = preferences.getLastTime()
        if (currentTime.isNullOrEmpty() || isFetchNeeded(currentTime)) {
            val notes = apiRequest { api.getNotes(userId) }
            noteList.postValue(notes.notes)
        }
    }

    private fun saveNotes(notes: List<Note>) = Coroutines.io {
        preferences.saveLastTime(System.currentTimeMillis().toString())
        db.getNoteDao().saveAllNotes(notes)
    }
}