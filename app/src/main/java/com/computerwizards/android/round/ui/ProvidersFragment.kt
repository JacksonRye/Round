package com.computerwizards.android.round.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.computerwizards.android.round.R
import com.computerwizards.android.round.adapters.ProvidersAdapter
import com.computerwizards.android.round.binding.FragmentDataBindingComponent
import com.computerwizards.android.round.databinding.ListFragmentBinding
import com.computerwizards.android.round.utils.EventObserver
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ProvidersFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    private val args by navArgs<ProvidersFragmentArgs>()

    private lateinit var providersRecyclerView: RecyclerView

    private val dataBindingComponent by lazy {
        FragmentDataBindingComponent(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ListFragmentBinding>(
            inflater,
            R.layout.list_fragment,
            container,
            false,
            dataBindingComponent
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ProvidersFragment.viewModel
        }


        providersRecyclerView = binding.providerRecyclerView

        return binding.root
    }

    private fun setupNavigation() {
        viewModel.userClickedEvent.observe(viewLifecycleOwner, EventObserver {
            val action = ProvidersFragmentDirections.showUser(it)
            findNavController().navigate(action)
        })
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getProviders(args.serviceUid)

        val providersAdapter = ProvidersAdapter(dataBindingComponent, null)

        providersRecyclerView.adapter = providersAdapter

        setupNavigation()
    }

}
