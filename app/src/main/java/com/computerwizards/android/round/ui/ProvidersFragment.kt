package com.computerwizards.android.round.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.computerwizards.android.round.adapters.ProvidersAdapter
import com.computerwizards.android.round.databinding.ListFragmentBinding
import javax.inject.Inject

class ProvidersFragment : ListFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    private val args by navArgs<ProvidersFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ListFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        this.binding = binding

        this.recyclerView = binding.recyclerView

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.query = firestore.collection("services")
            .document(args.serviceUid).collection("providers")


        this.adapter = object : ProvidersAdapter(query, viewModel) {}
        recyclerView.adapter = this.adapter as ProvidersAdapter
    }

    companion object {
        private const val TAG = "ProvidersFragment"
    }


}
