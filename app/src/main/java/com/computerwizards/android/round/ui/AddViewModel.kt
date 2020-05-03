package com.computerwizards.android.round.ui

import androidx.lifecycle.ViewModel
import com.computerwizards.android.model.Service
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddViewModel @Inject constructor(val firestore: FirebaseFirestore) : ViewModel() {

    private val servicesRef = firestore.collection("services").document()

    val newService = com.computerwizards.android.model.Service()

    fun addService(service: com.computerwizards.android.model.Service) {
        firestore.runTransaction { transaction ->

            service.apply { uid = servicesRef.id }

            transaction.set(servicesRef, service)
        }


    }

}
