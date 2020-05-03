package com.computerwizards.android.round.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.computerwizards.android.round.R
import com.computerwizards.android.round.databinding.ItemMediaBinding
import com.computerwizards.android.model.WorkMedia

class MediaAdapter(
    private val dataBindingComponent: DataBindingComponent,
    private val mediaCallback: ((com.computerwizards.android.model.WorkMedia) -> Unit)?
) : DataBoundListAdapter<com.computerwizards.android.model.WorkMedia, ItemMediaBinding>(DiffCallback) {

    override fun createBinding(parent: ViewGroup): ItemMediaBinding {
        val binding = DataBindingUtil.inflate<ItemMediaBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_media,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.media?.let {
                mediaCallback?.invoke(it)

            }
        }
        return binding
    }

    override fun bind(binding: ItemMediaBinding, item: com.computerwizards.android.model.WorkMedia) {

        binding.media = item
    }

    companion object DiffCallback : DiffUtil.ItemCallback<com.computerwizards.android.model.WorkMedia>() {
        override fun areItemsTheSame(oldItem: com.computerwizards.android.model.WorkMedia, newItem: com.computerwizards.android.model.WorkMedia): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: com.computerwizards.android.model.WorkMedia, newItem: com.computerwizards.android.model.WorkMedia): Boolean {
            return oldItem.location == newItem.location
        }
    }


}