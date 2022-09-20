package com.bizmiz.moynaktour.core.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BestPlacesItem(
    val ivImage:Int,
    val tvName:String,
    val tvRating:String,
    val tvPlacesCategory:String
):Parcelable

