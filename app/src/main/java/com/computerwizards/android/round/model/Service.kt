package com.computerwizards.android.round.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Service(
    var name: String? = "",
    var uid: String? = ""
//    var providers: List<Provider>? = emptyList()
) : Parcelable