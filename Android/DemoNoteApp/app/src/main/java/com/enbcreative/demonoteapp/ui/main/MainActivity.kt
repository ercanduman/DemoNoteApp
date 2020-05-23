package com.enbcreative.demonoteapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.data.backup.data.NoteDataSource
import com.enbcreative.demonoteapp.utils.toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab_main_activity.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_view_notes.adapter = notesAdapter
        val notes = NoteDataSource().getNotes()
        notesAdapter.submitItems(notes)
        notesAdapter.listener = { _, note, position ->
            toast("${note.id} at $position clicked")
        }
    }

    private val notesAdapter by lazy { NotesAdapter() }
}