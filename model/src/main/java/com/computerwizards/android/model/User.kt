package com.computerwizards.android.model

import com.google.firebase.auth.FirebaseUser

data class User(
    var uid: String? = "",
    var displayName: String? = "",
    var email: String? = "",
    var displayImageUrl: String? = null,
    var about: String? = "",
    var instanceToken: String? = ""
) {

    constructor(user: FirebaseUser) : this(
        user.uid,
        user.displayName,
        user.email
    )

    constructor(map: HashMap<*, *>) : this(

        uid = map["uid"] as String?,
        email = map["email"] as String?,
        instanceToken = map["instanceToken"] as String?,
        about = map["about"] as String?,
        displayImageUrl = map["displayImageUrl"] as String?,
        displayName = map["displayName"] as String?
    )
}