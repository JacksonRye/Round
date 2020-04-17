package com.computerwizards.android.round.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.computerwizards.android.round.adapters.ListAdapter
import com.computerwizards.android.round.databinding.ListFragmentBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.android.support.DaggerFragment

abstract class ListFragment : DaggerFragment() {

    abstract val viewModel: HomeViewModel

    var firestore: FirebaseFirestore = Firebase.firestore

    lateinit var recyclerView: RecyclerView

    open lateinit var adapter: ListAdapter

    abstract var query: Query

    lateinit var binding: ListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        recyclerView = binding.recyclerView

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        adapter = object : ServiceAdapter(query, viewModel) {}

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = adapter as ServiceAdapter
    }

    override fun onStart() {
        super.onStart()

        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        adapter.stopListening()
    }
}