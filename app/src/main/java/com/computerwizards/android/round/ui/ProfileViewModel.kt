package com.computerwizards.android.round.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.Service
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.model.WorkMedia
import com.computerwizards.android.round.utils.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    val firestore: FirebaseFirestore, val user: User?,
    val storage: FirebaseStorage
) :
    ViewModel(),
    Servicable {

    init {
        if (user == null) {
            throw IllegalStateException("A null user should not access profile")
        }
        Log.d("ProfileViewModel", "${user}")

        getPhotos()
    }

    private val _photoRefs = MutableLiveData<List<StorageReference>>()
    val photoRefs: LiveData<List<WorkMedia>> = Transformations.map(_photoRefs) { storageRefs ->
        val workMedia = mutableListOf<WorkMedia>()
        for (ref in storageRefs) {
            workMedia.add(WorkMedia(ref, user?.uid))
        }
        Log.d(TAG, "workMedia: $workMedia")
        workMedia.toList()
    }


    private fun getPhotos() {
        storage.reference.child("photos").listAll().addOnSuccessListener { listResult ->
            Log.d(TAG, "Storage Refs: ${listResult.items}")
            _photoRefs.value = listResult.items.toMutableList()
        }
    }

    private val _openAddServiceEvent = MutableLiveData<Event<Unit>>()
    val openAddServiceEvent: LiveData<Event<Unit>> = _openAddServiceEvent


    //    Test Query
//    val myServicesQuery: Query =
//        firestore.collection("users").document("1HbhZTiDUpgZBcVjY8D1").collection("services")

    val myServicesQuery: Query =
        firestore.collection("users").document(user?.uid!!).collection("services")


    override fun openService(service: Service) {
    }

    fun openServicesDialog() {
        Log.d(TAG, "viewmodel:openServicesDialog() called")
        _openAddServiceEvent.value = Event(Unit)
    }

    private val _profileImageClicked = MutableLiveData<Boolean>().apply { value = false }
    val profileImageClicked: LiveData<Boolean> = _profileImageClicked


    fun onProfileImageClicked() {
        _profileImageClicked.value = !profileImageClicked.value!!
    }


    companion object {
        private const val TAG = "ProfileViewMode"
    }


}
