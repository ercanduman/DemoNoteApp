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
        val currentNote = getCurrentItem(position)
        holder.binding.note = currentNote
        holder.binding.root.setOnClickListener {
            listener?.invoke(it, currentNote, position)
        }
    }

    fun getCurrentItem(position: Int): Note = itemList[position]
    fun removeItem(note: Note) = itemList.remove(note)
    fun addItem(note: Note) = itemList.add(note)
}