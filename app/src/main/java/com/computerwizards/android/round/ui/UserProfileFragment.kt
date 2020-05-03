package com.computerwizards.android.round.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.computerwizards.android.round.R
import com.computerwizards.android.round.adapters.MediaAdapter
import com.computerwizards.android.round.adapters.ServiceAdapter
import com.computerwizards.android.round.binding.FragmentDataBindingComponent
import com.computerwizards.android.round.databinding.UserProfileFragmentBinding
import com.computerwizards.android.round.utils.getService
import dagger.android.support.DaggerFragment
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserProfileFragment : DaggerFragment() {

    private lateinit var binding: UserProfileFragmentBinding

    private lateinit var servicesAdapter: ServiceAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<UserProfileViewModel> { viewModelFactory }

    private val args by navArgs<UserProfileFragmentArgs>()

    private lateinit var photoRecyclerView: RecyclerView

    private lateinit var serviceRecyclerView: RecyclerView

    private lateinit var userUid: String

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userUid = args.userUid

        if (userUid == viewModel.loggedInUser.uid) {
            val action = UserProfileFragmentDirections.showLoggedInProfile()
            findNavController().navigate(action)
        }


        binding = DataBindingUtil.inflate<UserProfileFragmentBinding>(
            inflater,
            R.layout.user_profile_fragment,
            container,
            false,
            dataBindingComponent
        ).apply {

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            lifecycleOwner = viewLifecycleOwner

        }

        setupViewModel()

        viewModel.uid = userUid

        binding.viewModel = viewModel

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.move)
        // When the image is loaded, set the image request listener to start the transaction
        binding.imageRequestListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                return false
            }
        }
        // Make sure we don't wait longer than a second for the image request
        postponeEnterTransition(1, TimeUnit.SECONDS)

        return binding.root
    }

    private fun setupViewModel() {
        viewModel.getProfileUser(userUid)
        viewModel.loggedInUserLikes(userUid)
        viewModel.getPhotos(userUid)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        photoRecyclerView = binding.photoRecyclerView
        serviceRecyclerView = binding.servicesRecyclerView

        val photoAdapter = MediaAdapter(dataBindingComponent, null)

        photoRecyclerView.adapter = photoAdapter

        servicesAdapter = object : ServiceAdapter(getService(userUid), viewModel) {}

        serviceRecyclerView.adapter = servicesAdapter


    }

    override fun onStart() {
        super.onStart()

        servicesAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        servicesAdapter.stopListening()
    }


}
