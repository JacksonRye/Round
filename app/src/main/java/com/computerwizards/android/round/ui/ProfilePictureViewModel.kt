package com.computerwizards.android.round.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.repository.UserRepository
import com.computerwizards.android.round.utils.Event
import timber.log.Timber
import javax.inject.Inject

class ProfilePictureViewModel @Inject constructor(
    val user: User,
    userRepository: UserRepository
) : ViewModel() {

    init {
        userRepository.getUserFromCloud(user.uid!!).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.e("liveDataUser: ${task.exception}")
            }
            _liveDataUser.value = task.result
        }
    }

    private val _liveDataUser = MutableLiveData<User>()
    val liveDataUser: LiveData<User> = _liveDataUser

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
