package com.computerwizards.android.round.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.computerwizards.android.round.databinding.ItemServiceBinding
import com.computerwizards.android.round.model.Service
import com.computerwizards.android.round.ui.HomeViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

open class ServiceAdapter(query: Query, private val viewModel: HomeViewModel) :
    FireAdapter<ServiceAdapter.ViewHolder>(query), ListAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getSnapshot(position).toObject<Service>()

        if (item != null) {
            holder.bind(item, viewModel)
        }
    }

    class ViewHolder private constructor(val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(service: Service, viewModel: HomeViewModel) {

            binding.viewModel = viewModel
            binding.service = service
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemServiceBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }


}