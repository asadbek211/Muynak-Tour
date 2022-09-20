package com.bizmiz.moynaktour.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupActionBarWithNavController
import com.bizmiz.moynaktour.MainActivity
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.utils.viewBinding
import com.bizmiz.moynaktour.databinding.FragmentBottomNavBinding
import com.bizmiz.moynaktour.databinding.FragmentCompassBinding

class BottomNavFragment : Fragment(R.layout.fragment_bottom_nav) {
    private lateinit var navController: NavController
    private val binding by viewBinding { FragmentBottomNavBinding.bind(it) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(),R.id.bottomContainer)
        (activity as MainActivity).setSupportActionBar(binding.toolBar)
        (activity as MainActivity).setupActionBarWithNavController(navController)
        setupSmoothBottomMenu()
    }
    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(requireContext(), null)
        popupMenu.inflate(R.menu.nav_menu)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }
}