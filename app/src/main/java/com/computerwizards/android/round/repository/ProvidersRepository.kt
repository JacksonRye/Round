package com.computerwizards.android.round.repository

import com.computerwizards.android.round.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import timber.log.Timber
import javax.inject.Inject

class ProvidersRepository @Inject constructor(
    val functions: FirebaseFunctions
) {

    fun getListOfProviders(serviceId: String): Task<List<User>> {

        val data = hashMapOf(
            "serviceId" to serviceId
        )

        return functions
            .getHttpsCallable("getProviders")
            .call(data)
            .continueWith { task ->
                if (!task.isSuccessful) {
                    throw Exception("ProvidersRepo:getListOfProviders:", task.exception)
                }

                val userList = task.result?.data as List<HashMap<*, *>>

                val result = mutableListOf<User>()

                userList.forEach { user ->
                    result.add(User(user))
                }

                Timber.d("getListOfProviders:result: $result")

                result
            }


    }

}