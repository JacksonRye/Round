package com.computerwizards.android.round.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.repository.UserRepository
import com.computerwizards.android.round.utils.Event
import javax.inject.Inject

class ProfilePictureViewModel @Inject constructor(
    val user: User,
    val userRepository: UserRepository
) : ViewModel() {

    val liveDataUser = userRepository.getUser(user.uid!!)

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


}
