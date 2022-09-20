package com.bizmiz.moynaktour.ui.compass

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.models.PlacesResponseByDistance
import com.bizmiz.moynaktour.core.utils.ResourceState
import com.bizmiz.moynaktour.core.utils.getDistance
import com.bizmiz.moynaktour.core.utils.networkCheck
import com.bizmiz.moynaktour.core.utils.viewBinding
import com.bizmiz.moynaktour.databinding.FragmentCompassBinding
import com.bizmiz.moynaktour.ui.home.HomeViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.DecimalFormat


class CompassFragment : Fragment(R.layout.fragment_compass) {
    private val binding by viewBinding { FragmentCompassBinding.bind(it) }
    private val homeViewModel:HomeViewModel by viewModel()
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.getMarkerData()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         map()
        placesObservers()
//        val distance = getDistance(LatLng(43.76572803583452, 59.027634528569465),
//            LatLng(43.76658663899446, 59.028691348845584))
//        Log.d("point","distance: $distance")
//            calculationByDistance()


    }
    private fun map() {
      mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.uiSettings.isZoomControlsEnabled
            val myPosition = CameraPosition.Builder()
                .target(LatLng(43.76572803583452, 59.027634528569465)).zoom(13f).bearing(90f).tilt(30f).build()
            googleMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(myPosition)
            )
        }
    }
    private fun setMarkers(image:String,lat:Double,lon:Double,markerName:String){
            Glide.with(requireContext())
                .asBitmap()
                .load(image)
                .into(object : SimpleTarget<Bitmap>() {
                    @Override
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val markerOptions = MarkerOptions()
                        val smallMarker1 = Bitmap.createScaledBitmap(resource, 200, 150, false)
                        markerOptions.position(LatLng(lat,lon))
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker1))
                        markerOptions.title(markerName)
                        mMap.addMarker(markerOptions)
                    }
                })
    }
    private fun placesObservers() {
        homeViewModel.markerData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    it.data?.forEach { response->
                        setMarkers(response.markerImage!!,response.lat!!.toDouble(),response.long!!.toDouble(),response.name!![0])
                    }
                    if (!networkCheck(requireContext())) {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.not_connect),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}