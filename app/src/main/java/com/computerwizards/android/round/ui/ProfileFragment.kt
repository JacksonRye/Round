package com.computerwizards.android.round.ui

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.computerwizards.android.round.adapters.ServiceAdapter
import com.computerwizards.android.round.databinding.ProfileFragmentBinding
import com.computerwizards.android.round.utils.EventObserver
import com.google.android.material.appbar.AppBarLayout
import javax.inject.Inject

// TODO: Create profile fragment, view to select what services you perform
// inspiration from pinterest add contributors

class ProfileFragment : ListFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<ProfileViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ProfileFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        this.binding = binding

        binding.viewModel = viewModel

        recyclerView = binding.recyclerView

        binding.profileImageClicked = viewModel.profileImageClicked


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        query = viewModel.myServicesQuery
        adapter = object : ServiceAdapter(query, viewModel) {}
        recyclerView.adapter = adapter as ServiceAdapter
        binding.lifecycleOwner = viewLifecycleOwner
        setupNavigation()

    }

    private val addServiceDialogFragment = AddServiceDialogFragment()

    private fun setupNavigation() {
        viewModel.openAddServiceEvent.observe(viewLifecycleOwner, EventObserver {
            Log.d(TAG, "observing openAddServiceEvent")
            addServiceDialogFragment.show(parentFragmentManager, "ProfileFragment")
        })
//        viewModel.closeServiceEvent.observe(viewLifecycleOwner, EventObserver {
//            addServiceDialogFragment.dismiss()
//        })
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }


}


class FixedAppBarLayoutBehaviour(context: Context, attrs: AttributeSet) :
    AppBarLayout.Behavior(context, attrs) {
    init {
        setDragCallback(object : DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean = false
        })
    }
}
