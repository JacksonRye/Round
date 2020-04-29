package com.computerwizards.android.round.binding

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.computerwizards.android.round.R
import com.computerwizards.android.round.adapters.MediaAdapter
import com.computerwizards.android.round.model.WorkMedia
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import java.io.InputStream
import javax.inject.Inject

class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        Glide.with(fragment).load(url).into(imageView)
    }

    @BindingAdapter("listData")
    fun bindRecyclerView(
        recyclerView: RecyclerView,
        data: List<WorkMedia>?
    ) {
        val adapter = recyclerView.adapter as MediaAdapter
        adapter.submitList(data)
    }

    @BindingAdapter(value = ["storageRef", "imageRequestListener"], requireAll = false)
    fun bindStorage(
        imageView: ImageView,
        storageReference: StorageReference?,
        listener: RequestListener<Drawable?>?
    ) {
        val options = RequestOptions()
            .error(R.drawable.ic_error)

        GlideApp.with(fragment).load(storageReference)
            .apply(options)
            .listener(listener).into(imageView)
    }
}

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}