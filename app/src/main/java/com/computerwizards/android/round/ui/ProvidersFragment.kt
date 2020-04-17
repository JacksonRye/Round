package com.computerwizards.android.round.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.computerwizards.android.round.databinding.ProvidersFragmentBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ProvidersFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ProvidersViewModel> { viewModelFactory }

    private val args by navArgs<ProvidersFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ProvidersFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel.serviceUid = args.serviceUid
        binding.viewModel = viewModel


        return binding.root


    }


}
