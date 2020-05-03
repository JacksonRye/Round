package com.computerwizards.android.round.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.computerwizards.android.round.R
import com.computerwizards.android.round.adapters.MediaAdapter
import com.computerwizards.android.round.adapters.ServiceAdapter
import com.computerwizards.android.round.binding.FragmentDataBindingComponent
import com.computerwizards.android.round.databinding.ProfileFragmentBinding
import com.computerwizards.android.round.utils.EventObserver
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.firestore.Query
import dagger.android.support.DaggerFragment
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ProfileFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private val dataBindingComponent: DataBindingComponent
            by lazy { FragmentDataBindingComponent(requireContext()) }
    private val viewModel by viewModels<ProfileViewModel> { viewModelFactory }

    private lateinit var servicesRecyclerView: RecyclerView

    private lateinit var photoRecyclerView: RecyclerView


    private lateinit var serviceAdapter: ServiceAdapter


    private lateinit var query: Query

    private lateinit var binding: ProfileFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<ProfileFragmentBinding>(
            inflater,
            R.layout.profile_fragment,
            container,
            false,
            dataBindingComponent
        )


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


//        viewModel.liveDataUser.observe(viewLifecycleOwner, Observer { user ->
//            Timber.d("user: $user")
//            binding.user = user
//        })

        binding.viewModel = viewModel

        servicesRecyclerView = binding.servicesRecyclerView

        photoRecyclerView = binding.photoRecyclerView



        binding.uploadButton.setOnClickListener {
            (activity as MainActivity).launchCamera()
        }

        binding.profileImageClicked = viewModel.profileImageClicked



        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        query = viewModel.myServicesQuery

        serviceAdapter = object : ServiceAdapter(query, viewModel) {}

        servicesRecyclerView.adapter = serviceAdapter

        photoRecyclerView.adapter = MediaAdapter(dataBindingComponent, null)

        binding.lifecycleOwner = viewLifecycleOwner
        setupNavigation()

    }

    private val addServiceDialogFragment = AddServiceDialogFragment()

    private fun setupNavigation() {
        viewModel.openAddServiceEvent.observe(viewLifecycleOwner, EventObserver {
            Timber.d("observing openAddServiceEvent")
            addServiceDialogFragment.show(parentFragmentManager, "ProfileFragment")
        })
        viewModel.openProfilePictureEvent.observe(viewLifecycleOwner, EventObserver {
            val action = ProfileFragmentDirections.viewProfilePicture()
            findNavController().navigate(action)
        })
    }

    override fun onStart() {
        super.onStart()

        serviceAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        serviceAdapter.stopListening()
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
