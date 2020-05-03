package com.computerwizards.android.round.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

val firestore = Firebase.firestore
val functions = Firebase.functions

fun getService(uid: String): Query {
    return firestore.collection("users").document(uid).collection("services")
}


fun updateDp(imageUrl: String): Task<Void> {

    val data = hashMapOf(
        "imageUrl" to imageUrl
    )

    return functions
        .getHttpsCallable("updateDp")
        .call(data)
        .continueWith { task ->

            if (!task.isSuccessful) {
                throw Exception(task.exception)
            }

            null
        }

}

private const val TAG = "FirebaseUtils"