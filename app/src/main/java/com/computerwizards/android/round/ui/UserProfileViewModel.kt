package com.computerwizards.android.round.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val loggedInUser: User
) : ViewModel() {

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile

    private val _loggedInLikes = MutableLiveData<Boolean>()
    val userLiked: LiveData<Boolean> = _loggedInLikes
    //
    private val _howManyLikes = MutableLiveData<String>()
    val howManyLikes: LiveData<String> = _howManyLikes


    fun getProfileUser(currentProfileId: String) {
        firestore.collection("users").document(currentProfileId)
            .get().addOnSuccessListener { documentSnapshot ->
                val userProfile = documentSnapshot.toObject<User>()!!
                Log.d("TAG", "getProfileUser: $userProfile")
                this._userProfile.value = userProfile
                Log.d("TAG", "_userProfile: ${_userProfile.value}")
            }
    }


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
