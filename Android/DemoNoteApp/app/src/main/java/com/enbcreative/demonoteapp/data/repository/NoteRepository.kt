package com.enbcreative.demonoteapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.data.db.model.scheduled.ScheduledNote
import com.enbcreative.demonoteapp.data.network.SafeApiRequest
import com.enbcreative.demonoteapp.data.network.WebApi
import com.enbcreative.demonoteapp.data.prefs.Preferences
import com.enbcreative.demonoteapp.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(
    private val db: AppDatabase,
    private val api: WebApi,
    private val preferences: Preferences,
    private val context: Context
) : SafeApiRequest() {
    private val noteList = MutableLiveData<List<Note>?>()

    init {
        noteList.observeForever { it?.let { notes -> saveNotes(notes) } }
    }

    fun saveNote(note: Note) = Coroutines.io { db.getNoteDao().insert(note) }
    fun updateNote(note: Note) = Coroutines.io { db.getNoteDao().update(note) }

    // fun deleteNote(note: Note) = Coroutines.io { db.getNoteDao().delete(note) }
    suspend fun getAllNotes(): LiveData<List<Note>> {
        return withContext(Dispatchers.IO) {
            val count = db.getNoteDao().getNoteCount()
            if (count == 0 && isNetworkAvailable(context).not()) {
                throw NoNetworkExceptions(context.getString(R.string.no_network))
            } else if (isNetworkAvailable(context)) fetchNotes()
            db.getNoteDao().getAllNotes(preferences.getUserID())
        }
    }

    private suspend fun fetchNotes() {
        val currentTime = preferences.getLastTime()
        if (currentTime.isNullOrEmpty() || isFetchNeeded(currentTime)) {
            val userId = preferences.getUserID()
            logd("Fetching note list for userId: $userId")
            val response = apiRequest { api.getNotes(userId) }
            logd("Api response message: ${response.message}")
            if (response.error.not()) {
                val notes = response.notes!!
                logd("Api response fetched note list size: ${notes.size}")
                notes.forEach { it.published = true }
                noteList.postValue(notes)
            } else throw ApiException(response.message)
        }
    }

    private fun saveNotes(notes: List<Note>) = Coroutines.io {
        preferences.saveLastTime(System.currentTimeMillis().toString())
        db.getNoteDao().saveAllNotes(notes)
    }

    fun saveScheduled(note: ScheduledNote) = Coroutines.io { db.getNoteDao().insert(note) }
    fun synchronizeAllNotes() {
        if (isNetworkAvailable(context)) {
            publishNotes()
            synchronizeNotes()
        } else logd("No network available. Cannot synchronize notes...")
    }

    private fun publishNotes() = Coroutines.io {
        val unpublishedNoteList = db.getNoteDao().getScheduledNotes()
        logd("Un published note size: ${unpublishedNoteList.size}")
        if (unpublishedNoteList.isNotEmpty()) {
            unpublishedNoteList.forEach { note ->
                val response =
                    apiRequest { api.insertNewNote(note.userId, note.content, note.created_at) }
                logd(response.message)
                if (response.error.not()) db.getNoteDao().deleteScheduledNote(note)
            }
        } else logd("All notes are published...")
    }

    private fun synchronizeNotes() = Coroutines.io {
        val unpublishedNoteList = db.getNoteDao().getUnPublishedNotes()
        logd("Un synchronized note size: ${unpublishedNoteList.size}")
        if (unpublishedNoteList.isNotEmpty()) {
            unpublishedNoteList.forEach { note ->
                if (note.crudOperation == Note.UPDATE) {
                    val response = apiRequest {
                        api.updateNote(note.id, note.userId, note.content, note.created_at)
                    }
                    logd(response.message)
                    if (response.error.not()) {
                        note.published = true
                        db.getNoteDao().update(note)
                    }
                } else if (note.crudOperation == Note.DELETE) {
                    val response = apiRequest { api.deleteNote(note.id, note.userId) }
                    logd(response.message)
                    if (response.error.not()) db.getNoteDao().delete(note)
                } else throw IllegalArgumentException("Invalid CRUD operation found as ${note.crudOperation}")
            }
        } else logd("All notes are synchronized...")
    }
}