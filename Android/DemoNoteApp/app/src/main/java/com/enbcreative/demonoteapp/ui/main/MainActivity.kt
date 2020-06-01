package com.enbcreative.demonoteapp.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.enbcreative.demonoteapp.DATE_FORMAT
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.data.db.model.scheduled.ScheduledNote
import com.enbcreative.demonoteapp.data.prefs.Preferences
import com.enbcreative.demonoteapp.ui.main.notes.NotesAdapter
import com.enbcreative.demonoteapp.ui.main.notes.NotesViewModel
import com.enbcreative.demonoteapp.ui.main.notes.NotesViewModelFactory
import com.enbcreative.demonoteapp.ui.splash.SplashActivity
import com.enbcreative.demonoteapp.utils.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.dialog_add_note.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), KodeinAware, SwipeRefreshLayout.OnRefreshListener {
    override val kodein by closestKodein()
    private val factory by instance<NotesViewModelFactory>()
    private val preferences by instance<Preferences>()

    private lateinit var viewModel: NotesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab_main_activity.setOnClickListener { upsertNote(null) }
        swipe_to_refresh_layout_main.setOnRefreshListener(this)
        viewModel = ViewModelProvider(this, factory).get(NotesViewModel::class.java)
        bindData()
    }

    private fun bindData() = Coroutines.main {
        progress_bar_loading_main.show()
        try {
            viewModel.synchronizeData()
            viewModel.notes.await().observe(this, Observer { handleData(it) })
        } catch (e: ApiException) {
            showContent(false, e.message)
            e.printStackTrace()
        } catch (e: Exception) {
            showContent(false, e.message)
            e.printStackTrace()
        }
        if (isNetworkAvailable(this).not()) parent_content_main_activity.snackbar(getString(R.string.no_network))
        else parent_content_main_activity.snackbar(getString(R.string.refreshed))
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
        progress_bar_loading_main.hide()
        swipe_to_refresh_layout_main.isRefreshing = false
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out_main -> showSignOutDialog()
            R.id.synchronize_main -> bindData()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSignOutDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.title_dialog_sign_out))
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                preferences.signOut()
                Intent(this, SplashActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(this)
                }
            }.create().show()
    }

    private fun upsertNote(currentNote: Note?) {
        val inflater = LayoutInflater.from(this)

        @SuppressLint("InflateParams")
        val dialog: View = inflater.inflate(R.layout.dialog_add_note, null)

        val title: TextView = dialog.dialog_note_title
        title.text =
            if (currentNote != null) getString(R.string.edit_note) else getString(R.string.new_note)

        val content: EditText = dialog.dialog_note_content
        if (currentNote != null) content.setText(currentNote.content)

        val changeTime = getCurrentDate()
        logd("Change Time: $changeTime")
        val builder = AlertDialog.Builder(this)
            .setView(dialog)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.save)) { d, _ ->
                if (currentNote != null) {
                    currentNote.content = content.text.toString()
                    currentNote.updated_at = changeTime
                    currentNote.published = false
                    currentNote.crudOperation = Note.UPDATE
                    Coroutines.main { viewModel.update(currentNote) }
                        .invokeOnCompletion {
                            d.dismiss()
                            toast("Note Updated")
                        }
                } else {
                    val scheduledNote = ScheduledNote(
                        userId = preferences.getUserID(),
                        content = content.text.toString(),
                        created_at = changeTime
                    )
                    Coroutines.main { viewModel.saveScheduled(scheduledNote) }
                        .invokeOnCompletion {
                            d.dismiss()
                            toast("New Note Added")
                        }
                }
            }
        builder.create().show()
    }

    private fun swipeToDelete() {
        logd("swipeToDelete() - called")
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                logd("onSwiped() - called")
                val position = viewHolder.adapterPosition
                val note = notesAdapter.getCurrentItem(position)
                notesAdapter.removeItem(note)
                notesAdapter.notifyItemRemoved(position)
                deleteConfirmSnackbar(note, position)
            }
        }).attachToRecyclerView(recycler_view_notes)
    }

    private fun deleteConfirmSnackbar(currentNote: Note, position: Int) {
        val message = getString(R.string.note_deleted)
        Snackbar.make(parent_content_main_activity, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.undo)) {
                // toast("Undo clicked")
                notesAdapter.addItem(currentNote)
                notesAdapter.notifyItemInserted(position)
            }
            .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        // toast("Snackbar closed on its own")
                        currentNote.published = false
                        currentNote.crudOperation = Note.DELETE
                        viewModel.update(currentNote)
                    }
                }
            }).show()
    }

    private fun getCurrentDate() = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
    override fun onRefresh() {
        bindData()
    }
}