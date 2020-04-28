package com.computerwizards.android.round.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.computerwizards.android.round.R

class ProfilePictureFragment : Fragment() {

    companion object {
        fun newInstance() = ProfilePictureFragment()
    }

    private lateinit var viewModel: ProfilePictureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_picture_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfilePictureViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
