package com.computerwizards.android.round.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.Service
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.utils.Event
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ServiceOptionsViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val user: User?
) : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _deleteServiceEvent = MutableLiveData<Event<Service>>()
    val deleteServiceEvent: LiveData<Event<Service>> = _deleteServiceEvent

    fun onClose() {
        _closeEvent.value = Event(Unit)
    }

    fun onDelete(service: Service) {
        firestore.collection("users").document(user?.uid!!)
            .collection("services").document(service.uid!!).delete()

        firestore.collection("services").document(service.uid!!)
            .collection("providers").document(user.uid!!).delete()


        _deleteServiceEvent.value = Event(service)
    }


}
