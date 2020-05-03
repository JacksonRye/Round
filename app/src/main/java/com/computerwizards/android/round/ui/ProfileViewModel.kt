package com.computerwizards.android.round.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.computerwizards.android.model.Service
import com.computerwizards.android.model.User
import com.computerwizards.android.model.WorkMedia
import com.computerwizards.android.round.repository.UserRepository
import com.computerwizards.android.round.utils.Event
import com.computerwizards.android.round.utils.getService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val user: com.computerwizards.android.model.User,
    val storage: FirebaseStorage,
    val userRepository: UserRepository
) :
    ViewModel(),
    Servicable {

    init {
        userRepository.getUserFromCloud(user.uid!!).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.e("init:getUserFromCloud: ${task.exception}")
            }

            _liveDataUser.value = task.result
        }

        getPhotos()
    }


    private val _liveDataUser = MutableLiveData<com.computerwizards.android.model.User>()
    val liveDataUser: LiveData<com.computerwizards.android.model.User> = _liveDataUser

    private val _photoRefs = MutableLiveData<List<StorageReference>>()
    val photoRefs: LiveData<List<com.computerwizards.android.model.WorkMedia>> = Transformations.map(_photoRefs) { storageRefs ->
        val workMedia = mutableListOf<com.computerwizards.android.model.WorkMedia>()
        for (ref in storageRefs) {
            workMedia.add(
                com.computerwizards.android.model.WorkMedia(
                    ref,
                    user.uid
                )
            )
        }
        Timber.d("workMedia: $workMedia")
        workMedia.toList()
    }

    private val _openProfilePictureEvent = MutableLiveData<Event<Unit>>()
    val openProfilePictureEvent: LiveData<Event<Unit>> = _openProfilePictureEvent


    private fun getPhotos() {
        storage.reference.child("photos/${user.uid}").listAll().addOnSuccessListener { listResult ->
            Timber.d("Storage Refs: ${listResult.items}")
            _photoRefs.value = listResult.items.toMutableList()
        }
    }

    private val _openAddServiceEvent = MutableLiveData<Event<Unit>>()
    val openAddServiceEvent: LiveData<Event<Unit>> = _openAddServiceEvent


    val myServicesQuery: Query = getService(user.uid!!)

    override fun openService(service: com.computerwizards.android.model.Service) {
    }

    fun openServicesDialog() {
        Timber.d("viewmodel:openServicesDialog() called")
        _openAddServiceEvent.value = Event(Unit)
    }

    private val _profileImageClicked = MutableLiveData<Boolean>().apply { value = false }
    val profileImageClicked: LiveData<Boolean> = _profileImageClicked


    fun onProfileImageClicked() {
        _profileImageClicked.value = !profileImageClicked.value!!
    }

    fun openProfilePicture() {
        _openProfilePictureEvent.value = Event(Unit)
    }


    companion object {
        private const val TAG = "ProfileViewModel"
    }


}
