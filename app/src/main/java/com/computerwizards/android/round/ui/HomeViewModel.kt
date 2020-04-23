package com.computerwizards.android.round.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.Service
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.utils.Event
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.iid.FirebaseInstanceId
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val firestore: FirebaseFirestore) : ViewModel(),
    Servicable {


    init {
        provideFirebaseInstanceId()
    }

    private fun provideFirebaseInstanceId() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstancefailed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                val msg = "InstanceID Token: {$token}"
                Log.d(TAG, msg)

                if (token != null) {
                    saveToken(token)
                }
            })
    }


    lateinit var instanceId: String

    private fun saveToken(token: String) {
        instanceId = token
    }

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

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            usersDocRef = firestore.collection("users").document(user.uid)

            createUser(User(user))
        }
    }

    private fun createUser(user: User): Task<Void> {

        return firestore.runTransaction { transaction ->

            user.instanceToken = instanceId

            transaction.set(usersDocRef, user)

            null
        }
    }

    fun onClicked(userId: String) {
        Log.d(TAG, "InstanceId: ${provideFirebaseInstanceId()}")

        _userClickedEvent.value = Event(userId)
    }

    override fun openService(service: Service) {
        Log.d(TAG, "InstanceId: ${provideFirebaseInstanceId()}")

        _openServiceEvent.value =
            Event(service)
//        showSnackbarMessage("Service: ${service.name} clicked")
    }

    private fun showSnackbarMessage(message: String) {
        _snackbarText.value =
            Event(message)
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}
