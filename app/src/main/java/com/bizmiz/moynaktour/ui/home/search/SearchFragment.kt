package com.bizmiz.moynaktour.ui.home.search

import android.app.Activity
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
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
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
import com.bizmiz.moynaktour.databinding.FragmentSearchBinding
import com.bizmiz.moynaktour.ui.home.HomeViewModel
import com.bizmiz.moynaktour.ui.home.best_places.BestPlacesAdapter
import com.bizmiz.moynaktour.ui.home.category.CategoryAdapter
import com.bizmiz.moynaktour.ui.home.nearby_places.NearbyPlacesAdapter
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var nearbyPlacesAdapter: NearbyPlacesAdapter
    private val binding by viewBinding { FragmentSearchBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nearbyPlacesAdapter = NearbyPlacesAdapter()
        binding.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(v)
                if (binding.etSearch.text.isNotEmpty()) {
                    val query = binding.etSearch.text.toString()
                    viewModel.getDataSearch(query)
                } else {
                    binding.searchRecyclerview.visibility = View.GONE
                    binding.txtYet.visibility = View.GONE
                }
                return@OnEditorActionListener true
            }
            false
        })
        binding.etSearch.showSoftKeyboard()
        binding.searchRecyclerview.adapter = nearbyPlacesAdapter

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        placesObservers()
        nearbyPlacesAdapter.onClickListener { data ->
            val bundle = bundleOf(
                "data" to data
            )
            val navController = Navigation.findNavController(requireActivity(), R.id.mainContainer)
            navController.navigate(R.id.action_searchFragment_to_details, bundle)
        }
        binding.imgBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainContainer)
            navController.popBackStack()
        }
    }

    private fun placesObservers() {
        viewModel.searchData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    if (it.data?.isNotEmpty() == true) {
                        binding.txtYet.visibility = View.GONE
                        binding.searchRecyclerview.visibility = View.VISIBLE
                        val prefs = binding.root.context.getSharedPreferences(
                            "MY_LOC",
                            Context.MODE_PRIVATE
                        )
                        val myLat = prefs.getString("lat", null)
                        val myLong = prefs.getString("long", null)
                        val list: ArrayList<PlacesResponseByDistance> = arrayListOf()
                        it.data.forEach { response ->
                            if (myLat != null && myLong != null) {
                                val distance = getDistance(
                                    LatLng(response.lat!!.toDouble(), response.long!!.toDouble()),
                                    LatLng(myLat.toDouble(), myLong.toDouble())
                                )
                                list.add(
                                    PlacesResponseByDistance(
                                        response.id,
                                        response.categoryId,
                                        response.name,
                                        response.about,
                                        response.rating,
                                        response.streetName,
                                        response.lat,
                                        response.long,
                                        response.markerImage,
                                        response.imageUrls,
                                        response.workTime,
                                        response.categoryName,
                                        distance
                                    )
                                )
                            }
                        }
                        nearbyPlacesAdapter.nearbyPlacesList = list.sortedBy { it.distance }
                        if (!networkCheck(requireContext())) {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.not_connect),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        binding.searchRecyclerview.visibility = View.GONE
                        binding.txtYet.visibility = View.VISIBLE
                    }

                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager? =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}