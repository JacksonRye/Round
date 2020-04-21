package com.computerwizards.android.round.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.Service
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.utils.Event
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val firestore: FirebaseFirestore) : ViewModel(),
    Servicable {

    val serviceQuery: Query = firestore.collection("services")

    private val _openServiceEvent = MutableLiveData<Event<Service>>()
    val openServiceEvent: LiveData<Event<Service>> = _openServiceEvent

    private val _userClickedEvent = MutableLiveData<Event<String>>()
    val userClickedEvent: LiveData<Event<String>> = _userClickedEvent

    var isSigningIn: Boolean = false

    val providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    private lateinit var usersDocRef: DocumentReference

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>> = _snackbarText

    fun createNewUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val user = User(firebaseUser)
            usersDocRef = firestore.collection("users").document(user.uid!!)

            createUser(user)
        }
    }

    private fun createUser(user: User): Task<Void> {

        return firestore.runTransaction { transaction ->

            transaction.set(usersDocRef, user)

            null
        }
    }

    fun onClicked(userId: String) {
        _userClickedEvent.value = Event(userId)
    }

    override fun openService(service: Service) {
        _openServiceEvent.value =
            Event(service)
//        showSnackbarMessage("Service: ${service.name} clicked")
    }

    private fun showSnackbarMessage(message: String) {
        _snackbarText.value =
            Event(message)
    }

}
