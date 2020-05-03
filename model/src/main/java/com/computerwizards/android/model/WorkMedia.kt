package com.computerwizards.android.model

import com.google.firebase.storage.StorageReference

data class WorkMedia(
    var location: StorageReference? = null,
    var postedBy: String? = ""
)