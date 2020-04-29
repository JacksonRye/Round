package com.computerwizards.android.round.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.computerwizards.android.round.R
import com.computerwizards.android.round.binding.FragmentDataBindingComponent
import com.computerwizards.android.round.databinding.ProfilePictureFragmentBinding
import com.computerwizards.android.round.utils.EventObserver
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ProfilePictureFragment : DaggerFragment() {

    private var fileUri: Uri? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ProfilePictureViewModel by viewModels { viewModelFactory }

    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ProfilePictureFragmentBinding>(
            inflater,
            R.layout.profile_picture_fragment,
            container,
            false,
            dataBindingComponent
        ).apply {
            viewModel = this@ProfilePictureFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        setupNavigation()




        return binding.root
    }

    private fun setupNavigation() {
        viewModel.updateProfilePictureEvent.observe(viewLifecycleOwner, EventObserver {
            ProfilePictureBottomSheet().show(parentFragmentManager, TAG)
        })

    }


    companion object {
        private const val TAG = "ProfilePictureFragment"
    }
}


