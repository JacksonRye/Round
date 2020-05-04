package com.computerwizards.android.round.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.computerwizards.android.round.R
import com.computerwizards.android.round.databinding.ItemUserBinding
import com.computerwizards.android.round.model.User

class ProvidersAdapter(
    private val dataBindingComponent: DataBindingComponent,
    private val providerCallback: ((User) -> Unit)?
) : DataBoundListAdapter<User, ItemUserBinding>(DiffCallBack) {


    override fun createBinding(parent: ViewGroup): ItemUserBinding {
        val binding = DataBindingUtil.inflate<ItemUserBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_user,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.user.let {
                providerCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: ItemUserBinding, item: User) {
        binding.user = item
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }


}