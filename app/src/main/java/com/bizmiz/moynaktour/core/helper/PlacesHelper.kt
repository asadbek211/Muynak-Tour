package com.bizmiz.moynaktour.core.helper


import android.content.SharedPreferences
import android.util.Log
import com.bizmiz.moynaktour.core.api.ApiInterface
import com.bizmiz.moynaktour.core.models.PlacesResponse
import com.bizmiz.moynaktour.core.utils.Constant
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import com.bizmiz.moynaktour.core.models.api.*
import com.google.firebase.firestore.Query


class PlacesHelper(private val apiClient: Retrofit, private val db: FirebaseFirestore) {

    fun getPlaceData(
        onSuccess: (list: ArrayList<PlacesResponse>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<PlacesResponse> = arrayListOf()
        db.collection(Constant.BASE_COLLECTION).orderBy("rating", Query.Direction.DESCENDING).get().addOnSuccessListener {
                it.documents.forEach { doc ->
                    val model = doc.toObject(PlacesResponse::class.java)
                    if (model != null) {
                        list.add(model)
                    }
                }
                onSuccess.invoke(list)
            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun getFavData(
        prefs: SharedPreferences,
        onSuccess: (list: ArrayList<PlacesResponse>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<PlacesResponse> = arrayListOf()
        db.collection(Constant.BASE_COLLECTION).get().addOnSuccessListener {
            it.documents.forEach { doc ->
                val model = doc.toObject(PlacesResponse::class.java)
                if (model != null && prefs.contains(model.id.toString())) {
                    list.add(model)
                }
            }
            onSuccess.invoke(list)
        }.addOnFailureListener {
            onFailure.invoke(it.localizedMessage)
        }
    }
    fun getDataByCatId(
        categoryId:Int,
        onSuccess: (list: ArrayList<PlacesResponse>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<PlacesResponse> = arrayListOf()
        db.collection(Constant.BASE_COLLECTION).get().addOnSuccessListener {
            it.documents.forEach { doc ->
                val model = doc.toObject(PlacesResponse::class.java)
                if (model != null && model.categoryId==categoryId) {
                    list.add(model)
                }
            }
            onSuccess.invoke(list)
        }.addOnFailureListener {
            onFailure.invoke(it.localizedMessage)
        }
    }
    fun getDataSearch(
        text:String,
        onSuccess: (list: ArrayList<PlacesResponse>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<PlacesResponse> = arrayListOf()
        db.collection(Constant.BASE_COLLECTION).get().addOnSuccessListener {
            it.documents.forEach { doc ->
                val model = doc.toObject(PlacesResponse::class.java)
                if (model?.name != null && model.categoryName != null) {
                    if (model.name[0].lowercase().contains(text.lowercase()) || model.name[1].lowercase().contains(text.lowercase())
                        || model.name[2].lowercase().contains(text.lowercase())|| model.name[3].lowercase().contains(text.lowercase())
                        || model.categoryName[0].lowercase().contains(text.lowercase()) || model.categoryName[1].lowercase().contains(text.lowercase())
                        || model.categoryName[2].lowercase().contains(text.lowercase())|| model.categoryName[3].lowercase().contains(text.lowercase())

                    ){
                        list.add(model)
                    }
                }
            }
            onSuccess.invoke(list)
        }.addOnFailureListener {
            onFailure.invoke(it.localizedMessage)
        }
    }
    fun getMarker(
        onSuccess: (list: ArrayList<PlacesResponse>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<PlacesResponse> = arrayListOf()
        db.collection(Constant.BASE_COLLECTION).get().addOnSuccessListener {
            it.documents.forEach { doc ->
                val model = doc.toObject(PlacesResponse::class.java)
                if (model != null) {
                    list.add(model)
                }
            }
            onSuccess.invoke(list)
        }.addOnFailureListener {
            onFailure.invoke(it.localizedMessage)
        }
    }
    fun getDistrict(
        format: String, lat: String, lon: String, onSuccess: (district: Response<Result>?) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val call = apiClient.create(ApiInterface::class.java).getDistrict(format,lat,lon)
        call.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>?, response: Response<Result>?) {
                onSuccess.invoke(response)
            }

            override fun onFailure(call: Call<Result>?, t: Throwable?) {
                onFailure.invoke(t?.localizedMessage)
            }

        })
    }
}