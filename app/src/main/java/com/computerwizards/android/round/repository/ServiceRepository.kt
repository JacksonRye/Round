package com.computerwizards.android.round.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import javax.inject.Inject

class ServiceRepository @Inject constructor(
    val functions: FirebaseFunctions
) {

    fun addService(serviceUid: String): Task<Void> {

        val data = hashMapOf(
            "serviceUid" to serviceUid
        )

        return functions
            .getHttpsCallable("addService")
            .call(data)
            .continueWith { task ->
                if (!task.isSuccessful) {
                    throw Exception("ServiceRepository:addService: ", task.exception)
                }
                null
            }

    }


}