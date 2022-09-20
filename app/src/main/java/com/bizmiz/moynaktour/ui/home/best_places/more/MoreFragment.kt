package com.bizmiz.moynaktour.ui.home.best_places.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupActionBarWithNavController
import com.bizmiz.moynaktour.MainActivity
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.models.PlacesResponseByDistance
import com.bizmiz.moynaktour.core.utils.viewBinding
import com.bizmiz.moynaktour.databinding.*
import com.bizmiz.moynaktour.ui.home.nearby_places.NearbyPlacesAdapter

class MoreFragment : Fragment(R.layout.fragment_more) {
    private lateinit var nearbyPlacesAdapter:NearbyPlacesAdapter
    private var bestPlaceList:List<PlacesResponseByDistance>? = null
    private val binding by viewBinding { FragmentMoreBinding.bind(it) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nearbyPlacesAdapter = NearbyPlacesAdapter()
        bestPlaceList = requireArguments().get("bestPlaceList") as List<PlacesResponseByDistance>?
        binding.moreNearbyRecView.adapter = nearbyPlacesAdapter
        nearbyPlacesAdapter.nearbyPlacesList = bestPlaceList!!
        nearbyPlacesAdapter.onClickListener {data->
            val bundle = bundleOf(
                "data" to data
            )
            val navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.navigate(R.id.action_more_to_details,bundle)
        }
        binding.btnBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.popBackStack()
        }
    }
}