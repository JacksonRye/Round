package com.computerwizards.android.round.utils

import com.computerwizards.android.round.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

val firestore = Firebase.firestore
val functions = Firebase.functions

fun getService(uid: String): Query {
    return firestore.collection("users").document(uid).collection("services")
}

fun getUserDocRef(userUid: String): DocumentReference {
    return firestore.collection("users").document(userUid)
}

fun saveUser(user: User, userDocRef: DocumentReference): Task<Void> {
    return firestore.runTransaction { transaction ->
        transaction.set(userDocRef, user)

        null
    }
}

fun updateProfilePicture(uid: String): Task<Void> {
    // create the arguments to the callable function.
    val data = hashMapOf(
        "uid" to uid
    )

    return functions
        .getHttpsCallable("updateProfilePicture")
        .call(data)
        .continueWith { task ->
            // This continuation runs on either success or failure, but if the task
            // has failed then result will throw an Exception which will be
            // propagated down.
            if (!task.isSuccessful) {
                throw Exception(task.exception)
            }

            null
        }
}

private const val TAG = "FirebaseUtils"