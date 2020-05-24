package com.enbcreative.demonoteapp.data.db.model.note

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note)

    @Insert
    fun saveAllNotes(notes: List<Note>)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM Note ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT COUNT(*) FROM Note")
    fun getNoteCount(): Int
}