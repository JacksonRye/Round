package com.computerwizards.android.round.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.computerwizards.android.round.adapters.ServiceAdapter
import com.computerwizards.android.round.databinding.AddServiceFragmentBinding
import com.computerwizards.android.round.utils.EventObserver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.Query
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AddServiceDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AddServiceViewModel> { viewModelFactory }

    lateinit var query: Query

    private lateinit var adapter: ServiceAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = AddServiceFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        binding.viewModel = viewModel

        recyclerView = binding.recyclerView

        return binding.root
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        query = viewModel.allServicesQuery
        adapter = object : ServiceAdapter(query, viewModel) {}
        recyclerView.adapter = adapter
        setup()
    }

    private fun setup() {
        viewModel.closeServiceEvent.observe(viewLifecycleOwner, EventObserver {
            dismiss()
        })
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