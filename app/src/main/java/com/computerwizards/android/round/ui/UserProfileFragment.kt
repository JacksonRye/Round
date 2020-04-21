package com.computerwizards.android.round.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.computerwizards.android.round.databinding.UserProfileFragmentBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class UserProfileFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<UserProfileViewModel> { viewModelFactory }

    private val args by navArgs<UserProfileFragmentArgs>()

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (args.userUid == viewModel.loggedInUser?.uid) {
            val action = UserProfileFragmentDirections.showLoggedInProfile()
            findNavController().navigate(action)
        }


        val binding = UserProfileFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        recyclerView = binding.recyclerView


        viewModel.getProfileUser(args.userUid)

        binding.userProfile = viewModel.userProfile

        binding.viewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner


        val mainActivity = activity as MainActivity

        mainActivity.setSupportActionBar(binding.toolbar)

        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)



        return binding.root
    }


}
