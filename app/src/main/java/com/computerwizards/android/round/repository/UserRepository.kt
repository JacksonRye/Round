package com.computerwizards.android.round.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.computerwizards.android.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.functions.FirebaseFunctions
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    val firestore: FirebaseFirestore,
    val functions: FirebaseFunctions
) {


//    fun getUser

    fun getUser(userId: String): LiveData<com.computerwizards.android.model.User> {
        val data = MutableLiveData<com.computerwizards.android.model.User>()

        firestore.collection("users").document(userId)
            .get().addOnSuccessListener { documentSnapshot ->
                data.value = documentSnapshot.toObject<com.computerwizards.android.model.User>()
            }

        return data
    }

    fun getUserFromCloud(userId: String): Task<com.computerwizards.android.model.User> {
        val data = hashMapOf(
            "uid" to userId
        )

        return functions
            .getHttpsCallable("getUser")
            .call(data)
            .continueWith { task ->
                if (!task.isSuccessful) {
                    throw Exception("UserRepo:getUserFromCloud", task.exception)
                }

                val result = task.result?.data as HashMap<*, *>

                val user = com.computerwizards.android.model.User(result)

                Timber.d("getUserFromCloud:result: ${result}")

                user
            }

    }

}