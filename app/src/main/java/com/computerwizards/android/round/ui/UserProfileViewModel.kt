package com.computerwizards.android.round.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.Service
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.model.WorkMedia
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val loggedInUser: User,
    val storage: FirebaseStorage
) : ViewModel(), Servicable {

    lateinit var uid: String

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile

    private val _loggedInLikes = MutableLiveData<Boolean>()
    val userLiked: LiveData<Boolean> = _loggedInLikes
    //
    private val _howManyLikes = MutableLiveData<String>()
    val howManyLikes: LiveData<String> = _howManyLikes


    private val _photoRefs = MutableLiveData<List<StorageReference>>()
    val photoRefs: LiveData<List<WorkMedia>> = Transformations.map(_photoRefs) { storageRefs ->
        val workMedia = mutableListOf<WorkMedia>()
        for (ref in storageRefs) {
            workMedia.add(WorkMedia(ref, uid))

        }
        Log.d(TAG, "workMedia: $workMedia")
        workMedia.toList()
    }

    fun getPhotos(uid: String) {
        storage.reference.child("photos/$uid").listAll().addOnSuccessListener { listResult ->
            Log.d(TAG, "Storage Refs: ${listResult.items}")
            _photoRefs.value = listResult.items.toMutableList()
        }
    }


    fun getProfileUser(currentProfileId: String) {
        firestore.collection("users").document(currentProfileId)
            .get().addOnSuccessListener { documentSnapshot ->
                val userProfile = documentSnapshot.toObject<User>()!!
                Log.d("TAG", "getProfileUser: $userProfile")
                this._userProfile.value = userProfile
                Log.d("TAG", "_userProfile: ${_userProfile.value}")
            }
    }

    override fun openService(service: Service) {}

    fun addToLike(userId: String) {

        Log.d(TAG, "addToLike:LoggedInUsersLike: ${userLiked.value}")

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
