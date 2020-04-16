package com.computerwizards.android.round.ui

import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.Service
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddViewModel @Inject constructor(val firestore: FirebaseFirestore) : ViewModel() {

    private val servicesRef = firestore.collection("services").document()

    val newService = Service()

    fun addService(service: Service) {
        firestore.runTransaction { transaction ->

            service.apply { uid = servicesRef.id }

            transaction.set(servicesRef, service)
        }


    }

}
