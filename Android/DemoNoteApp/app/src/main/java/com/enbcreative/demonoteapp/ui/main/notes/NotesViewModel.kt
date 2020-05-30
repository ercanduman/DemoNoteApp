package com.enbcreative.demonoteapp.ui.main.notes

import androidx.lifecycle.ViewModel
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.data.db.model.scheduled.ScheduledNote
import com.enbcreative.demonoteapp.data.repository.NoteRepository
import com.enbcreative.demonoteapp.utils.lazyDeferred

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {
    val notes by lazyDeferred { repository.getAllNotes() }
    fun saveScheduled(scheduled: ScheduledNote) = repository.saveScheduled(scheduled)
    fun update(note: Note) = repository.updateNote(note)
    fun delete(note: Note) = repository.deleteNote(note)

    init {
        repository.publishNotes()
        repository.synchronizeNotes()
    }

    suspend fun synchronizeData() {
        repository.publishNotes()
        repository.synchronizeNotes()
        repository.getAllNotes()
    }
}