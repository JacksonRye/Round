package com.computerwizards.android.round.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class DataBoundListAdapter<T, V : ViewDataBinding>(
    val diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, DataBoundViewHolder<V>>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {

        val binding = createBinding(parent)
        return DataBoundViewHolder(binding)
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        Log.d("DataBoundLA", "getItem: ${getItem(position)}")
        bind(holder.binding, getItem(position))
        holder.binding.executePendingBindings()
    }

    protected abstract fun bind(binding: V, item: T)
}