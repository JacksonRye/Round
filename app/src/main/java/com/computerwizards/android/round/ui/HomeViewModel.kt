package com.computerwizards.android.round.ui

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.Event
import com.computerwizards.android.round.R
import com.computerwizards.android.round.model.Service
import com.computerwizards.android.round.model.User
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val firestore: FirebaseFirestore) : ViewModel() {

    private val _openServiceEvent = MutableLiveData<Event<Service>>()
    val openServiceEvent: LiveData<Event<Service>> = _openServiceEvent

    var isSigningIn: Boolean = false

    val providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    private val usersRef = firestore.collection("users").document()

//    val servicesRef: Query = firestore.collection("services")

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarText

    fun createNewUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val user = User(firebaseUser).apply { uid = usersRef.id }
            createUser(user)
        }
    }

    private fun createUser(user: User): Task<Void> {

        return firestore.runTransaction { transaction ->

            transaction.set(usersRef, user)

            null
        }
    }

    fun openService(service: Service) {
        _openServiceEvent.value = Event(service)
        showSnackbarMessage(R.string.service)
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }

}
