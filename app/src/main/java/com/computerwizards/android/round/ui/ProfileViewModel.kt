package com.computerwizards.android.round.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.Service
import com.computerwizards.android.round.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class ProfileViewModel @Inject constructor(val firestore: FirebaseFirestore, val user: User?) :
    ViewModel(),
    Servicable {

    init {
        if (user == null) {
            throw IllegalStateException("A null user should not access profile")
        }
        Log.d("ProfileViewModel", "${user}")
    }

    //    Test Query
    val servicesQuery: Query =
        firestore.collection("users").document("1HbhZTiDUpgZBcVjY8D1").collection("services")

//    val servicesQuery: Query = firestore.collection("users").document(user?.uid!!).collection("services")


    override fun openService(service: Service) {

    }
}
