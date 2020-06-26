package com.example.pricequote.ui.auth

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    var uid: String? = null,
    var name: String? = null,
    var email: String? = null,
    @Exclude
    var isAuthenticated: Boolean = false,
    @Exclude
    var isNew: Boolean = false,
    @Exclude
    var isCreated: Boolean = false
) : Parcelable {
    constructor() : this(null, null, null, false, false, false)
}