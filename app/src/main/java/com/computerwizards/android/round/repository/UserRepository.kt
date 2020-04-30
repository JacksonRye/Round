package com.computerwizards.android.round.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.computerwizards.android.round.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    val firestore: FirebaseFirestore
) {


    fun getUser(userId: String): LiveData<User> {
        val data = MutableLiveData<User>()

        firestore.collection("users").document(userId)
            .get().addOnSuccessListener { documentSnapshot ->
                data.value = documentSnapshot.toObject<User>()
            }

        return data
    }

}