package com.computerwizards.android.round.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.utils.Event
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ProfilePictureViewModel @Inject constructor(
    val user: User,
    val storage: FirebaseStorage,
    val firestore: FirebaseFirestore
) : ViewModel() {


    private val _openCameraEvent = MutableLiveData<Event<Unit>>()
    val openCameraEvent: LiveData<Event<Unit>> = _openCameraEvent

    private val _openGalleryEvent = MutableLiveData<Event<Unit>>()
    val openGalleryEvent: LiveData<Event<Unit>> = _openGalleryEvent

    private val _updateProfilePictureEvent = MutableLiveData<Event<Unit>>()
    val updateProfilePictureEvent: LiveData<Event<Unit>> = _updateProfilePictureEvent


    fun updatePicture() {
        _updateProfilePictureEvent.value = Event(Unit)
    }

    fun launchCamera() {
        _openCameraEvent.value = Event(Unit)
    }

    fun openGallery() {
        _openGalleryEvent.value = Event(Unit)
    }

    val userDocRef = firestore.collection("users").document("${user.uid}")

    fun updateUser(user: User): Task<Void> {

        user.profilePictureStorageRef =
            storage.reference.child("photos/${user.uid}/profilePic")
                .listAll().result?.items?.last()

        return firestore.runTransaction { transaction ->

            transaction.set(userDocRef, user)


            null
        }
    }


}
