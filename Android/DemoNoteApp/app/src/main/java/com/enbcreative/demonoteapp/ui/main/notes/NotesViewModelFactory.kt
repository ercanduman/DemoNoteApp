package com.enbcreative.demonoteapp.ui.main.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enbcreative.demonoteapp.data.repository.NoteRepository

class NotesViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class found as : ${modelClass.name}")
    }
}