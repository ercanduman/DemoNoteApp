package com.enbcreative.demonoteapp.internal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerView<T : Any, VB : ViewDataBinding> :
    RecyclerView.Adapter<BaseRecyclerView.Companion.BaseViewHolder<VB>>() {

    fun submitItems(items: List<T>) {
        itemList = items as MutableList<T>
        notifyDataSetChanged()
    }

    abstract fun getLayout(): Int

    var listener: ((View, T, Int) -> Unit)? = null

    var itemList = mutableListOf<T>()
    override fun getItemCount() = itemList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder<VB>(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayout(), parent, false)
    )

    companion object {
        class BaseViewHolder<VB : ViewDataBinding>(val binding: VB) :
            RecyclerView.ViewHolder(binding.root)
    }
}