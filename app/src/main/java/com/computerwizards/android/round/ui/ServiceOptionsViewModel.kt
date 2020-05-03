package com.computerwizards.android.round.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.utils.Event
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ServiceOptionsViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val user: User
) : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _deleteServiceEvent = MutableLiveData<Event<String>>()
    val deleteServiceEvent: LiveData<Event<String>> = _deleteServiceEvent

    fun onClose() {
        _closeEvent.value = Event(Unit)
    }

    fun onDelete(serviceId: String) {
        firestore.collection("users").document(user.uid!!)
            .collection("services").document(serviceId).delete()

        firestore.collection("services").document(serviceId)
            .collection("providers").document(user.uid!!).delete()


        _deleteServiceEvent.value = Event(serviceId)
    }


}
