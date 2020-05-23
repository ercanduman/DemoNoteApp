package com.enbcreative.demonoteapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.enbcreative.demonoteapp.R
import kotlinx.android.synthetic.main.fragment_note_details.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NoteDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val noteId = it.getString(BUNDLE_NOTE_ID)
            tv_notes_details_fragment_message.text = noteId.toString()
        }

        view.findViewById<Button>(R.id.btn_note_details_previous).setOnClickListener {
            findNavController().navigate(R.id.action_NoteDetailsFragment_to_NoteFragment)
        }
    }

    companion object {
        const val BUNDLE_NOTE_ID = "com.enbcreative.demonoteapp.BUNDLE_NOTE_ID"
    }
}
