package com.bizmiz.moynaktour.ui.settings

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
import com.bizmiz.moynaktour.databinding.FragmentHomeBinding
import com.bizmiz.moynaktour.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding by viewBinding { FragmentSettingsBinding.bind(it) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}