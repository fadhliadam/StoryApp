package com.adam.submissionstoryapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    var name:  String? = null,
    var userId:  String? = null,
    var token: String? = null
) : Parcelable

@Parcelize
data class StoryModel(
    var photoUrl: String? = null,
    var createdAt: String? = null,
    var name: String? = null,
    var description: String? = null,
    var lon: Double? = null,
    var id: String? = null,
    var lat: Double? = null
) : Parcelable