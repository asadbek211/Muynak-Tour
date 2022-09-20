package com.bizmiz.moynaktour.core.utils

import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.maps.model.LatLng
import com.orhanobut.hawk.Hawk
fun View.showSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}
fun networkCheck(context: Context): Boolean {
        val conManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo

        return internetInfo != null && internetInfo.isConnected
    }
 fun getLanguage():String {
    return Hawk.get("pref_lang","")

}
fun getDistance(LatLng1: LatLng, LatLng2: LatLng): Double {
    var distance = 0.0
    val locationA = Location("A")
    locationA.latitude = LatLng1.latitude
    locationA.longitude = LatLng1.longitude
    val locationB = Location("B")
    locationB.latitude = LatLng2.latitude
    locationB.longitude = LatLng2.longitude
    distance = locationA.distanceTo(locationB).toDouble()
    return distance
}
inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) = setOnClickListener { func() }