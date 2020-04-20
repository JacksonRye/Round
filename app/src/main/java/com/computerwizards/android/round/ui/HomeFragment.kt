package com.computerwizards.android.round.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.computerwizards.android.round.R
import com.computerwizards.android.round.adapters.ServiceAdapter
import com.computerwizards.android.round.databinding.HomeFragmentBinding
import com.computerwizards.android.round.utils.EventObserver
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: ServiceAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = HomeFragmentBinding.inflate(
            inflater,
            container,
            false
        )


        recyclerView = binding.recyclerView

        setHasOptionsMenu(true)

        (activity as MainActivity).setSupportActionBar(binding.homeToolbar)

        return binding.root

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.log_out -> {
                FirebaseAuth.getInstance().signOut()
                startSignIn()
                true
            }
            else -> true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val query = viewModel.serviceQuery
        adapter = object : ServiceAdapter(query, viewModel) {}
        recyclerView.adapter = adapter

        setupNavigation()
    }

    private fun setupNavigation() {
        viewModel.openServiceEvent.observe(viewLifecycleOwner,
            EventObserver {
                Log.d(TAG, "setupNav: $it")

                val action = HomeFragmentDirections.showProviders(it.uid!!)
                findNavController().navigate(action)
            })
    }


    override fun onStart() {
        super.onStart()

        // Start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn()
            return
        }

        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun shouldStartSignIn(): Boolean {
        return !viewModel.isSigningIn && FirebaseAuth.getInstance().currentUser == null
    }

    private fun startSignIn() {
        // Sign in with FirebaseUI
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(viewModel.providers)
            .setIsSmartLockEnabled(false)
            .build()

        startActivityForResult(intent, RC_SIGN_IN)
        viewModel.isSigningIn = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            viewModel.isSigningIn = false

            if (resultCode == Activity.RESULT_OK) {
                viewModel.createNewUser()
            } else {

            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "HomeFragment"
    }


}
