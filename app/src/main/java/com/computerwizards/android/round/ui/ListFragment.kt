package com.computerwizards.android.round.ui

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.computerwizards.android.round.adapters.ListAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.android.support.DaggerFragment

abstract class ListFragment : DaggerFragment() {

    abstract val viewModel: ViewModel

    var firestore: FirebaseFirestore = Firebase.firestore

    open lateinit var servicesRecyclerView: RecyclerView

    open lateinit var adapter: ListAdapter

    open lateinit var query: Query

    open lateinit var binding: ViewDataBinding

    override fun onStart() {
        super.onStart()

        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        adapter.stopListening()
    }
}