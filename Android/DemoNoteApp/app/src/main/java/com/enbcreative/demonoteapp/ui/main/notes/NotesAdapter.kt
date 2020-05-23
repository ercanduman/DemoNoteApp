package com.enbcreative.demonoteapp.ui.main.notes

import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.data.db.model.note.Note
import com.enbcreative.demonoteapp.databinding.ListItemNoteBinding
import com.enbcreative.demonoteapp.internal.BaseRecyclerView

class NotesAdapter : BaseRecyclerView<Note, ListItemNoteBinding>() {
    override fun getLayout() = R.layout.list_item_note

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<ListItemNoteBinding>,
        position: Int
    ) {
        val currentNote = itemList[position]
        holder.binding.note = currentNote
        holder.binding.root.setOnClickListener {
            listener?.invoke(it, currentNote, position)
        }
    }
}