package com.computerwizards.android.round.model

import com.google.firebase.auth.FirebaseUser

data class User(
    var uid: String? = "",
    var displayName: String? = "",
    var email: String? = ""
) {

    constructor(user: FirebaseUser) : this(
        user.uid,
        user.displayName,
        user.email
    )

}