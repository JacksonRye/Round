package com.computerwizards.android.round.utils

import com.computerwizards.android.round.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val firestore = Firebase.firestore

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

private const val TAG = "FirebaseUtils"