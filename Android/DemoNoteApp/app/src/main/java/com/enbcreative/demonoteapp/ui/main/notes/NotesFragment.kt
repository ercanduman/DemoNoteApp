package com.enbcreative.demonoteapp.ui.main.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.data.backup.data.NoteDataSource
import com.enbcreative.demonoteapp.ui.main.NoteDetailsFragment
import com.enbcreative.demonoteapp.utils.toast
import kotlinx.android.synthetic.main.fragment_note_list.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NotesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // findNavController().navigate(R.id.action_NotesFragment_to_NoteDetailsFragment)

        recycler_view_notes.adapter = notesAdapter
        val notes = NoteDataSource().getNotes()
        notesAdapter.submitItems(notes)
        notesAdapter.listener = { _, note, pos ->
            requireContext().toast("${note.id} at $pos clicked")
            val bundle = bundleOf(NoteDetailsFragment.BUNDLE_NOTE_ID to note.content)
            findNavController().navigate(R.id.action_NotesFragment_to_NoteDetailsFragment, bundle)
        }
    }

    private val notesAdapter by lazy { NotesAdapter() }
}
