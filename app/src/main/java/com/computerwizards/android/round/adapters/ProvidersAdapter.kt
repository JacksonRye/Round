package com.computerwizards.android.round.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.computerwizards.android.round.R
import com.computerwizards.android.round.binding.FragmentDataBindingComponent
import com.computerwizards.android.round.databinding.ItemUserBinding
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.ui.HomeViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import timber.log.Timber

open class ProvidersAdapter(query: Query, private val viewModel: HomeViewModel) :
    FireAdapter<ProvidersAdapter.ViewHolder>(query), ListAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getSnapshot(position).toObject<User>()

        item?.let { user ->
            holder.bind(user, viewModel)
        }

    }

    class ViewHolder private constructor(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, viewModel: HomeViewModel) {
            binding.viewModel = viewModel
            binding.user = user
            Timber.d("bind: $user")
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val dataBindingComponent = FragmentDataBindingComponent(parent.context)
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<ItemUserBinding>(
                    layoutInflater,
                    R.layout.item_user,
                    parent,
                    false,
                    dataBindingComponent
                )

                return ViewHolder(binding)
            }

        }
    }

    companion object {
        private const val TAG = "ProvidersAdapter"
    }


}