package com.enbcreative.demonoteapp.ui.main.notes

import androidx.lifecycle.ViewModel
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.data.repository.NoteRepository

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {
    fun save(note: Note) = repository.saveNote(note)
    fun update(note: Note) = repository.updateNote(note)
    fun delete(note: Note) = repository.deleteNote(note)
    fun getAllNotes() = repository.getAllNotes()
}