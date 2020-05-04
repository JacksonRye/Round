package com.computerwizards.android.round.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.Service
import com.computerwizards.android.round.repository.ServiceRepository
import com.computerwizards.android.round.utils.Event
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddServiceViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    private val serviceRepository: ServiceRepository
) : ViewModel(), Servicable {

    val allServicesQuery =
        firestore.collection("services")


    private val _closeServiceEvent = MutableLiveData<Event<Unit>>()
    val closeServiceEvent: LiveData<Event<Unit>> = _closeServiceEvent


    override fun openService(service: Service) {
        service.uid?.let { serviceRepository.addService(it) }
    }


    fun closeDialog() {
        _closeServiceEvent.value = Event(Unit)
    }

}