package com.enbcreative.demonoteapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val factory by instance<NotesViewModelFactory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab_main_activity.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        bindData()
    }

    private fun bindData() = Coroutines.main {
        val viewModel = ViewModelProvider(this, factory).get(NotesViewModel::class.java)
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
}