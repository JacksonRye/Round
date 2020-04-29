package com.computerwizards.android.round.model

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference

data class User(
    var uid: String? = "",
    var displayName: String? = "",
    var email: String? = "",
//    var displayImageUrl: String? = "",
    var profilePictureStorageRef: StorageReference? = null,
    var about: String? = "",
    var instanceToken: String? = ""
) {

    constructor(user: FirebaseUser) : this(
        user.uid,
        user.displayName,
        user.email
    )

}