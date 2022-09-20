package com.bizmiz.moynaktour.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.moynaktour.core.helper.PlacesHelper
import com.bizmiz.moynaktour.core.models.PlacesResponse
import com.bizmiz.moynaktour.core.utils.Resource
import retrofit2.Response
import com.bizmiz.moynaktour.core.models.api.*
class HomeViewModel(private val placesHelper: PlacesHelper) : ViewModel() {
    private val placeList: MutableLiveData<Resource<ArrayList<PlacesResponse>>> = MutableLiveData()
    val place: LiveData<Resource<ArrayList<PlacesResponse>>>
        get() = placeList
    private val setDistrict: MutableLiveData<Resource<Response<Result>?>> = MutableLiveData()
    val district: LiveData<Resource<Response<Result>?>>
        get() = setDistrict
    private val favList: MutableLiveData<Resource<ArrayList<PlacesResponse>>> = MutableLiveData()
    val fav: LiveData<Resource<ArrayList<PlacesResponse>>>
        get() = favList
    private val catList: MutableLiveData<Resource<ArrayList<PlacesResponse>>> = MutableLiveData()
    val categoryData: LiveData<Resource<ArrayList<PlacesResponse>>>
        get() = catList
    private val marker: MutableLiveData<Resource<ArrayList<PlacesResponse>>> = MutableLiveData()
    val markerData: LiveData<Resource<ArrayList<PlacesResponse>>>
        get() = marker
    private val search: MutableLiveData<Resource<ArrayList<PlacesResponse>>> = MutableLiveData()
    val searchData: LiveData<Resource<ArrayList<PlacesResponse>>>
        get() = search
    fun getDistrict(format: String, lat: String, lon: String) {
        placesHelper.getDistrict(format, lat, lon, {
            setDistrict.value = Resource.success(it)
        }, {
            setDistrict.value = Resource.error(it)
        })
    }
    fun getPlaceData() {
        placesHelper.getPlaceData({
            placeList.value = Resource.success(it)
        }, {
            placeList.value = Resource.error(it)
        })
    }
    fun getFavData(
        prefs: SharedPreferences
    ) {
        placesHelper.getFavData(prefs,{
            favList.value = Resource.success(it)
        }, {
            favList.value = Resource.error(it)
        })
    }
    fun getCatData(
        categoryId:Int
    ) {
        placesHelper.getDataByCatId(categoryId,{
            catList.value = Resource.success(it)
        }, {
            catList.value = Resource.error(it)
        })
    }
    fun getMarkerData(
    ) {
        placesHelper.getMarker({
            marker.value = Resource.success(it)
        }, {
            marker.value = Resource.error(it)
        })
    }
    fun getDataSearch(
        text:String
    ) {
        placesHelper.getDataSearch(text,{
            search.value = Resource.success(it)
        }, {
            search.value = Resource.error(it)
        })
    }
}