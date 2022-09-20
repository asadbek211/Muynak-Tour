package com.bizmiz.moynaktour.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.models.*
import com.bizmiz.moynaktour.core.utils.*
import com.bizmiz.moynaktour.databinding.FragmentHomeBinding
import com.bizmiz.moynaktour.ui.home.best_places.BestPlacesAdapter
import com.bizmiz.moynaktour.ui.home.category.CategoryAdapter
import com.bizmiz.moynaktour.ui.home.nearby_places.NearbyPlacesAdapter
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import org.koin.android.viewmodel.ext.android.viewModel
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var bestPlaceList:List<PlacesResponseByDistance>? = null
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var bestPlacesAdapter: BestPlacesAdapter
    private lateinit var nearbyPlacesAdapter: NearbyPlacesAdapter
    private val binding by viewBinding { FragmentHomeBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPlaceData()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (networkCheck(requireContext())) {
            getLastLocation()
        } else {
            Toast.makeText(requireContext(), getString(R.string.not_connect), Toast.LENGTH_SHORT)
                .show()
            binding.tvLoc.setTextColor(resources.getColor(android.R.color.holo_red_light))
            binding.tvLoc.text = getString(R.string.location)
        }
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        categoryAdapter = CategoryAdapter()
        bestPlacesAdapter = BestPlacesAdapter()
        nearbyPlacesAdapter = NearbyPlacesAdapter()
        binding.categoryRecyclerview.adapter = categoryAdapter
        binding.bestPlacesRecView.adapter = bestPlacesAdapter
        binding.nearbyPlacesRecView.adapter = nearbyPlacesAdapter
        categoryAdapter.onClickListener {
            val bundle = bundleOf(
                "data" to it
            )
            val navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.navigate(R.id.action_bottomNav_to_categoryData,bundle)
        }
        addList()
        placesObservers()
        binding.homeRecommendedMore.setOnClickListener {
            val bundle = bundleOf(
                "bestPlaceList" to bestPlaceList
            )
            val navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.navigate(R.id.bottomNav_to_more,bundle)
        }
        bestPlacesAdapter.onClickListener {data->
            val bundle = bundleOf(
                "data" to data
            )
            val navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.navigate(R.id.bottomNav_to_details,bundle)
        }
        nearbyPlacesAdapter.onClickListener {data->
            val bundle = bundleOf(
                "data" to data
            )
            val navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.navigate(R.id.bottomNav_to_details,bundle)
        }
        binding.tvLoc.setOnClickListener {
            if (networkCheck(requireContext())) {
                getLastLocation()
            } else {
                Toast.makeText(requireContext(), getString(R.string.not_connect), Toast.LENGTH_SHORT)
                    .show()
                binding.tvLoc.setTextColor(resources.getColor(android.R.color.holo_red_light))
                binding.tvLoc.text = getString(R.string.location)
            }
        }
        binding.chatMainSearch.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.navigate(R.id.action_bottomNav_to_searchFragment)
        }
    }
    private fun addList(){
        val listImage:List<Int> = listOf(
            R.drawable.ic_hotel,
            R.drawable.ic_restaurant,
            R.drawable.emergency,
            R.drawable.tourism,
            R.drawable.ic_shop,
            R.drawable.ic_taxi,
            R.drawable.ic_fuel_station,
            R.drawable.ic_banking
        )
       val listName:List<String> = listOf(
            getString(R.string.hotel),
            getString(R.string.cafe),
           getString(R.string.emergency),
            getString(R.string.museum),
            getString(R.string.shops),
            getString(R.string.taxi),
           getString(R.string.zaprafka),
           getString(R.string.bank),
        )
        for (i in listImage.indices){
            categoryAdapter.categoryList.add(CategoryItem(listImage[i],listName[i]))
        }

    }
    private fun placesObservers() {
        viewModel.place.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    val  prefs = binding.root.context.getSharedPreferences("MY_LOC", Context.MODE_PRIVATE)
                    val myLat = prefs.getString("lat",null)
                    val myLong = prefs.getString("long",null)
                    val list:ArrayList<PlacesResponseByDistance> = arrayListOf()
                    it.data?.forEach { response->
                        if (myLat != null && myLong != null) {
                            val distance = getDistance(
                                LatLng(response.lat!!.toDouble(), response.long!!.toDouble()),
                                LatLng(myLat.toDouble(), myLong.toDouble())
                            )
                            list.add(PlacesResponseByDistance(response.id,response.categoryId,response.name,response.about,response.rating,response.streetName,response.lat,response.long,
                                response.markerImage,response.imageUrls,response.workTime,response.categoryName,distance))
                        }
                    }
                    nearbyPlacesAdapter.nearbyPlacesList= list.sortedBy {it.distance}
                    bestPlaceList = list.sortedByDescending {it.rating}
                    bestPlacesAdapter.bestPlacesList = bestPlaceList as List<PlacesResponseByDistance>
                        if (!networkCheck(requireContext())) {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.not_connect),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                ResourceState.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun getLastLocation() {
        if (checkPermission()) {
            if (isGPSEnable()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        newLocationData()
                    } else {
                        binding.tvLoc.setTextColor(resources.getColor(R.color.loc_color))
                        binding.tvLoc.text = getString(R.string.getting_loc)
                        viewModel.getDistrict(
                            "geocodejson",
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                       val  prefs = requireActivity().getSharedPreferences("MY_LOC", Context.MODE_PRIVATE)
                        prefs.edit().putString("lat",location.latitude.toString()).apply()
                        prefs.edit().putString("long",location.longitude.toString()).apply()
                        getDistrict()
                    }
                }
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermission()
        }
    }

    private fun newLocationData() {
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
            }
        }
        if (checkPermission()) fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun Fragment.isGPSEnable(): Boolean =
        requireContext().getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)

    private fun Context.getLocationManager() =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 1010
        )
    }
    private fun getDistrict() {
        var county = ""
        viewModel.district.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceState.SUCCESS -> {
                    it.data?.body()?.features?.forEach { feature ->
                        when {
                            feature.properties.geocoding.city != null -> {
                                county = feature.properties.geocoding.city
                            }
                            feature.properties.geocoding.county != null -> {
                                county = feature.properties.geocoding.county
                            }
                            else -> {
                                county = getString(R.string.location)
                            }
                        }
                        binding.tvLoc.setTextColor(resources.getColor(R.color.loc_color))
                        binding.tvLoc.text = county
                    }
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    binding.tvLoc.setTextColor(resources.getColor(android.R.color.holo_red_light))
                    binding.tvLoc.text = getString(R.string.location)
                }
            }
        })
    }
}