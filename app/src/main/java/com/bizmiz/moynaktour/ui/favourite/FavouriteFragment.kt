package com.bizmiz.moynaktour.ui.favourite

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupActionBarWithNavController
import com.bizmiz.moynaktour.MainActivity
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.models.PlacesResponseByDistance
import com.bizmiz.moynaktour.core.utils.*
import com.bizmiz.moynaktour.databinding.FragmentBottomNavBinding
import com.bizmiz.moynaktour.databinding.FragmentCompassBinding
import com.bizmiz.moynaktour.databinding.FragmentFavouriteBinding
import com.bizmiz.moynaktour.databinding.FragmentHomeBinding
import com.bizmiz.moynaktour.ui.home.HomeViewModel
import com.bizmiz.moynaktour.ui.home.nearby_places.NearbyPlacesAdapter
import com.google.android.gms.maps.model.LatLng
import org.koin.android.viewmodel.ext.android.viewModel

class FavouriteFragment : Fragment(R.layout.fragment_favourite) {
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var nearbyPlacesAdapter: FavNearbyAdapter
    private val binding by viewBinding { FragmentFavouriteBinding.bind(it) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs: SharedPreferences = requireActivity().getSharedPreferences(
            Constant.PREFS,
            Context.MODE_PRIVATE
        )
        viewModel.getFavData(prefs)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nearbyPlacesAdapter = FavNearbyAdapter()
        binding.favRecView.adapter = nearbyPlacesAdapter
        val prefs: SharedPreferences = requireActivity().getSharedPreferences(Constant.PREFS,
            Context.MODE_PRIVATE
        )
        nearbyPlacesAdapter.favListener { key, id, editor ->
            if (key){
                editor.remove(id).apply()
            }else{
                editor.putBoolean(id,true).apply()
            }
            viewModel.getFavData(prefs)
        }
        nearbyPlacesAdapter.onClickListener {data->
            val bundle = bundleOf(
                "data" to data
            )
            val navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.navigate(R.id.bottomNav_to_details,bundle)
        }
        favObservers()
    }
    private fun favObservers() {
        viewModel.fav.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data!=null && it.data.isNotEmpty()){
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
                                list.add(
                                    PlacesResponseByDistance(response.id,response.categoryId,response.name,response.about,response.rating,response.streetName,response.lat,response.long,
                                        response.markerImage,response.imageUrls,response.workTime,response.categoryName,distance)
                                )
                            }
                        }
                        binding.favRecView.visibility = View.VISIBLE
                        binding.tvYet.visibility = View.GONE
                        nearbyPlacesAdapter.nearbyPlacesList= list
                    }else{
                        binding.progress.visibility = View.GONE
                        binding.tvYet.visibility = View.VISIBLE
                        binding.favRecView.visibility = View.GONE
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
                    binding.progress.visibility = View.GONE
                    binding.tvYet.visibility = View.VISIBLE
                    binding.favRecView.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}