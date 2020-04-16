package com.computerwizards.android.round.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.computerwizards.android.round.databinding.AddFragmentBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class AddFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AddViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AddFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        binding.viewModel = viewModel
        binding.service = viewModel.newService

        return binding.root
    }


}
