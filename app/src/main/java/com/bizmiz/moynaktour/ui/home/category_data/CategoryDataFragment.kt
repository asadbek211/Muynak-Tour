package com.bizmiz.moynaktour.ui.home.category_data

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
import android.util.Log
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
import com.bizmiz.moynaktour.databinding.FragmentCatDataBinding
import com.bizmiz.moynaktour.databinding.FragmentHomeBinding
import com.bizmiz.moynaktour.ui.home.HomeViewModel
import com.bizmiz.moynaktour.ui.home.best_places.BestPlacesAdapter
import com.bizmiz.moynaktour.ui.home.category.CategoryAdapter
import com.bizmiz.moynaktour.ui.home.nearby_places.NearbyPlacesAdapter
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import org.koin.android.viewmodel.ext.android.viewModel
class CategoryDataFragment : Fragment(R.layout.fragment_cat_data) {
    private val viewModel: HomeViewModel by viewModel()
    private var position:Int? = null
    private lateinit var nearbyPlacesAdapter: NearbyPlacesAdapter
    private val binding by viewBinding { FragmentCatDataBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = requireArguments().getInt("data")
        viewModel.getCatData(position!!)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position?.let { category(it) }
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        nearbyPlacesAdapter = NearbyPlacesAdapter()
        binding.moreNearbyRecView.adapter = nearbyPlacesAdapter
        placesObservers()
        nearbyPlacesAdapter.onClickListener {data->
            val bundle = bundleOf(
                "data" to data
            )
            val navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.navigate(R.id.action_categoryData_to_details,bundle)
        }
        binding.btnBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.popBackStack()
        }
    }
    private fun placesObservers() {
        viewModel.categoryData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data?.isNotEmpty() == true){
                        binding.tvYet.visibility = View.GONE
                        binding.moreNearbyRecView.visibility = View.VISIBLE
                        val  prefs = binding.root.context.getSharedPreferences("MY_LOC", Context.MODE_PRIVATE)
                        val myLat = prefs.getString("lat",null)
                        val myLong = prefs.getString("long",null)
                        val list:ArrayList<PlacesResponseByDistance> = arrayListOf()
                        it.data.forEach { response->
                            if (myLat != null && myLong != null) {
                                val distance = getDistance(
                                    LatLng(response.lat!!.toDouble(), response.long!!.toDouble()),
                                    LatLng(myLat.toDouble(), myLong.toDouble())
                                )
                                list.add(PlacesResponseByDistance(response.id,response.categoryId,response.name,response.about,response.rating,response.streetName,response.lat,response.long,
                                    response.markerImage,response.imageUrls,response.workTime,response.categoryName,distance))
                            }
                        }
                        nearbyPlacesAdapter.nearbyPlacesList= list.sortedByDescending {it.rating}
                        if (!networkCheck(requireContext())) {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.not_connect),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else{
                        binding.moreNearbyRecView.visibility = View.GONE
                        binding.tvYet.visibility = View.VISIBLE
                    }

                }
                ResourceState.ERROR -> {
                    binding.tvYet.visibility = View.VISIBLE
                    binding.moreNearbyRecView.visibility = View.GONE
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun category(catId:Int){
        when(catId){
            1-> {
                binding.btnBack.text = binding.root.context.getString(R.string.hotel)
            }
            2-> {
                binding.btnBack.text = binding.root.context.getString(R.string.cafe)
            }
            3-> {
                binding.btnBack.text = binding.root.context.getString(R.string.emergency)
            }
            4-> {
                binding.btnBack.text = binding.root.context.getString(R.string.museum)
            }
            5-> {
                binding.btnBack.text = binding.root.context.getString(R.string.shops)
            }
            6-> {
                binding.btnBack.text = binding.root.context.getString(R.string.taxi)
            }
            7-> {
                binding.btnBack.text = binding.root.context.getString(R.string.zaprafka)
            }
            8-> {
                binding.btnBack.text = binding.root.context.getString(R.string.bank)
            }
        }
    }
}