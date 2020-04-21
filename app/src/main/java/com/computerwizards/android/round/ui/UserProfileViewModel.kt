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
    val loggedInUser: User?
) : ViewModel() {

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile


    fun getProfileUser(currentProfileId: String) {
        firestore.collection("users").document(currentProfileId)
            .get().addOnSuccessListener { documentSnapshot ->
                val userProfile = documentSnapshot.toObject<User>()!!
                Log.d("TAG", "getProfileUser: $userProfile")
                this._userProfile.value = userProfile
                Log.d("TAG", "_userProfile: ${_userProfile.value}")
            }
    }

    companion object {
        private const val TAG = "UserProfileViewModel"
    }


}
