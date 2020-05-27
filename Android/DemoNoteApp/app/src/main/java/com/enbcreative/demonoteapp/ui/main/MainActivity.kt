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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.enbcreative.demonoteapp.DATE_FORMAT
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.ui.main.notes.NotesAdapter
import com.enbcreative.demonoteapp.ui.main.notes.NotesViewModel
import com.enbcreative.demonoteapp.ui.main.notes.NotesViewModelFactory
import com.enbcreative.demonoteapp.utils.ApiException
import com.enbcreative.demonoteapp.utils.Coroutines
import com.enbcreative.demonoteapp.utils.toast
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
        fab_main_activity.setOnClickListener { upsertNote(null) }
        bindData()
    }

    private fun bindData() = Coroutines.main {
        try {
            viewModel = ViewModelProvider(this, factory).get(NotesViewModel::class.java)
            viewModel.notes.await().observe(this, Observer { handleData(it) })
        } catch (e: ApiException) {
            showContent(false, e.message)
            e.printStackTrace()
        } catch (e: Exception) {
            showContent(false, e.message)
            e.printStackTrace()
        }
    }

    private fun handleData(notes: List<Note>) {
        if (notes.isEmpty().not()) initRecyclerView(notes)
        else showContent(false, getString(R.string.no_data_found))
    }

    private fun initRecyclerView(notes: List<Note>) {
        showContent(true, null)
        recycler_view_notes.adapter = notesAdapter
        notesAdapter.submitItems(notes)
        notesAdapter.listener = { _, note, _ -> upsertNote(note) }
        swipeToDelete()
    }

    private fun showContent(dataExist: Boolean, message: String?) {
        if (dataExist) {
            recycler_view_notes.visibility = View.VISIBLE
            no_data_found_main.visibility = View.GONE
        } else {
            recycler_view_notes.visibility = View.GONE
            no_data_found_main?.also {
                it.visibility = View.VISIBLE
                it.text = message
            }
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
        } else currentNote = Note("", "")

        val builder = AlertDialog.Builder(this)
            .setView(dialog)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.save)) { d, _ ->
                currentNote.content = content.text.toString()
                currentNote.date = getCurrentDate()
                if (note != null) {
                    Coroutines.main { viewModel.update(currentNote) }
                        .invokeOnCompletion {
                            d.dismiss()
                            toast("Note Updated")
                        }
                } else {
                    Coroutines.main { viewModel.save(currentNote) }
                        .invokeOnCompletion {
                            d.dismiss()
                            toast("New Note Added")
                        }
                }
            }
        builder.create().show()
    }

    private fun swipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = notesAdapter.getCurrentItem(position)
                viewModel.delete(note)
                toast("Note deleted.")
            }
        }).attachToRecyclerView(recycler_view_notes)
    }

    private fun getCurrentDate() = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
}