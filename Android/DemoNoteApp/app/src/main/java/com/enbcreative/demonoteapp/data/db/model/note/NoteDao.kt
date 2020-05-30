package com.enbcreative.demonoteapp.data.db.model.note

import androidx.lifecycle.LiveData
import androidx.room.*
import com.enbcreative.demonoteapp.data.db.model.scheduled.ScheduledNote

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllNotes(notes: List<Note>)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM  Note WHERE userId =:userId AND  published = 1 ORDER BY id DESC")
    fun getAllNotes(userId: Int): LiveData<List<Note>>

    @Query("SELECT * FROM Note WHERE published = 0")
    fun getUnPublishedNotes(): List<Note>

    @Insert
    fun insert(scheduled: ScheduledNote)

    @Query("SELECT * FROM ScheduledNote")
    fun getScheduledNotes(): List<ScheduledNote>

    @Delete
    fun deleteScheduledNote(scheduled: ScheduledNote)

    @Query("SELECT COUNT(*) FROM Note")
    fun getNoteCount(): Int
}