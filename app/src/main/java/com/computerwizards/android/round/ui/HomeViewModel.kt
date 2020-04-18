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
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val firestore: FirebaseFirestore) : ViewModel(),
    Servicable {

    private val _openServiceEvent = MutableLiveData<Event<Service>>()
    val openServiceEvent: LiveData<Event<Service>> = _openServiceEvent

    var isSigningIn: Boolean = false

    val providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    private val usersDocRef = firestore.collection("users").document()
    val usersCollectionRef = firestore.collection("users")

//    val servicesRef: Query = firestore.collection("services")

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>> = _snackbarText

    fun createNewUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val user = User(firebaseUser).apply { uid = usersDocRef.id }
            createUser(user)
        }
    }

    private fun createUser(user: User): Task<Void> {

        return firestore.runTransaction { transaction ->

            transaction.set(usersDocRef, user)

            null
        }
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
