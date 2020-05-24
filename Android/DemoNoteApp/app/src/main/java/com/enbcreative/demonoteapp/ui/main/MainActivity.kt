package com.enbcreative.demonoteapp.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.enbcreative.demonoteapp.DATE_FORMAT
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.ui.main.notes.NotesAdapter
import com.enbcreative.demonoteapp.ui.main.notes.NotesViewModel
import com.enbcreative.demonoteapp.ui.main.notes.NotesViewModelFactory
import com.enbcreative.demonoteapp.utils.Coroutines
import com.enbcreative.demonoteapp.utils.toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.dialog_add_note.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val factory by instance<NotesViewModelFactory>()
    private lateinit var viewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab_main_activity.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            upsertNote(null)
        }
        bindData()
    }

    private fun bindData() = Coroutines.main {
        viewModel = ViewModelProvider(this, factory).get(NotesViewModel::class.java)
        viewModel.getAllNotes().observe(this, Observer { handleData(it) })
    }

    private fun handleData(notes: List<Note>) {
        toast("${notes.size} notes found!")
        if (notes.isEmpty().not()) initRecyclerView(notes)
        else dataExist(false)
    }

    private fun initRecyclerView(notes: List<Note>) {
        dataExist(true)
        recycler_view_notes.adapter = notesAdapter
        notesAdapter.submitItems(notes)
        notesAdapter.listener = { _, note, position ->
            toast("${note.id} at $position clicked")
            upsertNote(note)
        }
    }

    private fun dataExist(dataExist: Boolean) {
        if (dataExist) {
            recycler_view_notes.visibility = View.VISIBLE
            no_data_found_main.visibility = View.GONE
        } else {
            recycler_view_notes.visibility = View.GONE
            no_data_found_main.visibility = View.VISIBLE
        }
    }

    private val notesAdapter by lazy { NotesAdapter() }


    private fun upsertNote(note: Note?) {
        val inflater = LayoutInflater.from(this)

        @SuppressLint("InflateParams")
        val dialog: View = inflater.inflate(R.layout.dialog_add_note, null)

        val title: TextView = dialog.dialog_note_title
        val currentNote: Note?
        title.text =
            if (note != null) getString(R.string.edit_note) else getString(R.string.new_note)

        val content: EditText = dialog.dialog_note_content
        if (note != null) {
            currentNote = note
            content.setText(note.content)
        } else currentNote = Note("", getCurrentDate())

        val builder = AlertDialog.Builder(this)
            .setView(dialog)
            .setCancelable(false)
            .setNegativeButton("Cancel", null)
            .setPositiveButton(getString(R.string.save)) { d, _ ->
                toast("Save clicked")
                currentNote.content = content.text.toString()
                if (note != null) {
                    Coroutines.main {
                        viewModel.update(currentNote)
                    }.invokeOnCompletion {
                        d.dismiss()
                        toast("Note Updated")
                    }
                } else {
                    Coroutines.main {
                        viewModel.save(currentNote)
                    }.invokeOnCompletion {
                        toast("New Note Added")
                        d.dismiss()
                    }
                }
            }
        builder.create().show()
    }

    private fun getCurrentDate(): String = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
}