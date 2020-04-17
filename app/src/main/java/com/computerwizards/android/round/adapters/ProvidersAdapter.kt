package com.computerwizards.android.round.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.computerwizards.android.round.databinding.ItemUserBinding
import com.computerwizards.android.round.model.Provider
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.ui.HomeViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

open class ProvidersAdapter(query: Query, private val viewModel: HomeViewModel) :
    FireAdapter<ProvidersAdapter.ViewHolder>(query), ListAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getSnapshot(position).toObject<Provider>()

        item?.let { provider ->
            Log.d(TAG, "user: $provider")

            viewModel.usersCollectionRef.document(provider.userUid!!)
                .get().addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<User>()

                    if (user != null) {
                        holder.bind(user, viewModel)
                    }

                }


        }

    }

    class ViewHolder private constructor(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, viewModel: HomeViewModel) {
            binding.viewModel = viewModel
            binding.user = user
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemUserBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }

        }
    }

    companion object {
        private const val TAG = "ProvidersAdapter"
    }


}