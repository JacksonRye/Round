package com.computerwizards.android.round.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.computerwizards.android.model.Service
import com.computerwizards.android.model.User
import com.computerwizards.android.model.WorkMedia
import com.computerwizards.android.round.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val loggedInUser: com.computerwizards.android.model.User,
    val storage: FirebaseStorage,
    val userRepository: UserRepository
) : ViewModel(), Servicable {

    lateinit var uid: String

    private val _userProfile = MutableLiveData<com.computerwizards.android.model.User>()
    val userProfile: LiveData<com.computerwizards.android.model.User> = _userProfile

    private val _loggedInLikes = MutableLiveData<Boolean>()
    val userLiked: LiveData<Boolean> = _loggedInLikes
    //
    private val _howManyLikes = MutableLiveData<String>()
    val howManyLikes: LiveData<String> = _howManyLikes


    private val _photoRefs = MutableLiveData<List<StorageReference>>()
    val photoRefs: LiveData<List<com.computerwizards.android.model.WorkMedia>> = Transformations.map(_photoRefs) { storageRefs ->
        val workMedia = mutableListOf<com.computerwizards.android.model.WorkMedia>()
        for (ref in storageRefs) {
            workMedia.add(com.computerwizards.android.model.WorkMedia(ref, uid))

        }
        Timber.d("workMedia: $workMedia")
        workMedia.toList()
    }

    fun getPhotos(uid: String) {
        storage.reference.child("photos/$uid").listAll().addOnSuccessListener { listResult ->
            Timber.d("Storage Refs: ${listResult.items}")
            _photoRefs.value = listResult.items.toMutableList()
        }
    }


    fun getProfileUser(currentProfileId: String) {
        userRepository.getUserFromCloud(currentProfileId).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.e("getProfileUser: ${task.exception}")
            }
            _userProfile.value = task.result
            Timber.d("getProfileUser: ${_userProfile.value}")

        }
    }

    override fun openService(service: com.computerwizards.android.model.Service) {}

    fun addToLike(userId: String) {

        Timber.d("addToLike:LoggedInUsersLike: ${userLiked.value}")

        loggedInUserLikes(userId)

        val usersLikedCollection =
            firestore.collection("users").document(userId).collection("users-liked")

        val usersLikedDocumentRef = usersLikedCollection
            .document(loggedInUser.uid!!)

        val loggedInLikesDocumentRef =
            firestore.collection("users").document(loggedInUser.uid!!).collection("likes")
                .document(userId)

        if (!_loggedInLikes.value!!) {
            usersLikedDocumentRef.set(loggedInUser)

            loggedInLikesDocumentRef.set(userProfile.value!!)

        } else {
            usersLikedDocumentRef.delete()

            loggedInLikesDocumentRef.delete()
        }

        usersLikedCollection
            .get().addOnSuccessListener {
                _howManyLikes.value = it.size().toString()
            }
    }

    fun loggedInUserLikes(userId: String) {
        firestore.collection("users").document(loggedInUser.uid!!).collection("likes")
            .document(userId).get().addOnSuccessListener { documentSnapshot ->
                _loggedInLikes.value = documentSnapshot.exists()
            }
        firestore.collection("users").document(userId).collection("users-liked")
            .get().addOnSuccessListener {
                _howManyLikes.value = it.size().toString()
            }
    }

    companion object {
        private const val TAG = "UserProfileViewModel"
    }


}
