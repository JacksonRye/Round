package com.computerwizards.android.round.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.computerwizards.android.round.model.User
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

    fun getUser(userId: String): LiveData<User> {
        val data = MutableLiveData<User>()

        firestore.collection("users").document(userId)
            .get().addOnSuccessListener { documentSnapshot ->
                data.value = documentSnapshot.toObject<User>()
            }

        return data
    }

    fun getUserFromCloud(userId: String): Task<User> {
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

                val user = User(result)

                Timber.d("getUserFromCloud:result: ${result}")

                user
            }

    }

}