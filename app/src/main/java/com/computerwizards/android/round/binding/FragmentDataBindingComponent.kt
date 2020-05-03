package com.computerwizards.android.round.binding

import android.content.Context
import androidx.databinding.DataBindingComponent

class FragmentDataBindingComponent(context: Context) : DataBindingComponent {
    private val adapter = FragmentBindingAdapters(context)

    override fun getFragmentBindingAdapters() = adapter
}