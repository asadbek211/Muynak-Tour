package com.bizmiz.moynaktour.core.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlacesResponse(
    val id:Int? = null,
    val categoryId:Int? = null,
    val name:List<String>? = null,
    val about:List<String>? = null,
    val rating:String? = null,
    val streetName:List<String>? = null,
    val lat:String? = null,
    val long:String? = null,
    val markerImage:String? = null,
    val imageUrls:List<String>? = null,
    val workTime:List<String>? = null,
    val categoryName:List<String>? = null
): Parcelable
