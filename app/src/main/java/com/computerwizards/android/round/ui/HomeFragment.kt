package com.computerwizards.android.round.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.computerwizards.android.round.EventObserver
import com.computerwizards.android.round.R
import com.computerwizards.android.round.adapters.ListAdapter
import com.computerwizards.android.round.adapters.ServiceAdapter
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import javax.inject.Inject

class HomeFragment : ListFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    override var query: Query = firestore.collection("services")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
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

        var adapter: ListAdapter = object : ServiceAdapter(query, viewModel) {}
        this.adapter = adapter
        recyclerView.adapter = adapter as ServiceAdapter

        setupNavigation()
    }

    private fun setupNavigation() {
        viewModel.openServiceEvent.observe(viewLifecycleOwner, EventObserver {
            Log.d(TAG, "setupNav: $it")
//            Snackbar.make(
//                requireActivity().findViewById(android.R.id.content),
//                "Service: ${it.name} clicked", Snackbar.LENGTH_SHORT
//            ).show()
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
