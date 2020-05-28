package com.enbcreative.demonoteapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enbcreative.demonoteapp.DATA_FROM_ROOM
import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.data.network.SafeApiRequest
import com.enbcreative.demonoteapp.data.network.WebApi
import com.enbcreative.demonoteapp.data.prefs.Preferences
import com.enbcreative.demonoteapp.utils.ApiException
import com.enbcreative.demonoteapp.utils.Coroutines
import com.enbcreative.demonoteapp.utils.isFetchNeeded
import com.enbcreative.demonoteapp.utils.logd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(
    private val db: AppDatabase,
    private val api: WebApi,
    private val preferences: Preferences
) : SafeApiRequest() {
    private val noteList = MutableLiveData<List<Note>?>()

    init {
        noteList.observeForever { it?.let { notes -> saveNotes(notes) } }
    }

    fun saveNote(note: Note) = Coroutines.io { db.getNoteDao().insert(note) }
    fun updateNote(note: Note) = Coroutines.io { db.getNoteDao().update(note) }
    fun deleteNote(note: Note) = Coroutines.io { db.getNoteDao().delete(note) }
    suspend fun getAllNotes(): LiveData<List<Note>> {
        return withContext(Dispatchers.IO) {
            if (DATA_FROM_ROOM.not()) fetchNotes()
            db.getNoteDao().getAllNotes()
        }
    }

    private suspend fun fetchNotes() {
        val currentTime = preferences.getLastTime()
        if (currentTime.isNullOrEmpty() || isFetchNeeded(currentTime)) {
            val userId = preferences.getUserID()
            logd("Fetching note list for userId: $userId")
            val response = apiRequest { api.getNotes(userId) }
            logd("Api response message: ${response.message}")
            if (response.error.not()) noteList.postValue(response.notes)
            else throw ApiException(response.message)
        }
    }

    private fun saveNotes(notes: List<Note>) = Coroutines.io {
        preferences.saveLastTime(System.currentTimeMillis().toString())
        db.getNoteDao().saveAllNotes(notes)
    }
}