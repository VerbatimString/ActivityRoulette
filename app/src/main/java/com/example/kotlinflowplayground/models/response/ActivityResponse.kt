package com.example.kotlinflowplayground.models.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActivityResponse(
    val accessibility: Double?,
    val activity: String?,
    val key: String?,
    val link: String?,
    val participants: Int?,
    val price: Double?,
    val type: String?,
    val error: String?
) : Parcelable