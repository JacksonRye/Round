package com.computerwizards.android.round.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.Service
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.utils.Event
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

class AddServiceViewModel @Inject constructor(
    val firestore: FirebaseFirestore
    , val user: User?
) : ViewModel(), Servicable {


//    val selectedServiceRef = firestore.collection("services").document()

    init {
        Timber.d("$user")
    }


    val allServicesQuery =
        firestore.collection("services")


    private val _closeServiceEvent = MutableLiveData<Event<Unit>>()
    val closeServiceEvent: LiveData<Event<Unit>> = _closeServiceEvent


    override fun openService(service: Service) {
        addService(service)
    }

    private fun addService(service: Service): Task<Void> {
        return firestore.runTransaction { transaction ->

            val selectedServiceRef = firestore.collection("services")
                .document(service.uid!!).collection("providers").document(user?.uid!!)

            val userServicesRef = firestore.collection("users").document(user.uid!!)
                .collection("services").document(service.uid!!)




            transaction.set(userServicesRef, service)

            transaction.set(selectedServiceRef, user)

            null
        }
    }

    fun closeDialog() {
        _closeServiceEvent.value = Event(Unit)
    }

    companion object {
        private const val TAG = "AddServiceViewModel"
    }

}