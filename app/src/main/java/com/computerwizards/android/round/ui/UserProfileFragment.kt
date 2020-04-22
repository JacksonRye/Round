package com.computerwizards.android.round.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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
        ).apply {

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.getProfileUser(args.userUid)
        viewModel.loggedInUserLikes(args.userUid)

        binding.viewModel = viewModel

        binding.userProfile = viewModel.userProfile

        recyclerView = binding.recyclerView



        return binding.root
    }


}
