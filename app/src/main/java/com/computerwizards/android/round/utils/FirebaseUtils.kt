package com.computerwizards.android.round.utils

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val firestore = Firebase.firestore

fun getService(uid: String): Query {
    return firestore.collection("users").document(uid).collection("services")
}