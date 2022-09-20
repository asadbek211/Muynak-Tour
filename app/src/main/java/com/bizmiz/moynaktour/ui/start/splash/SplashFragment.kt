package com.bizmiz.moynaktour.ui.start.splash

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.utils.viewBinding
import com.bizmiz.moynaktour.databinding.FragmentSplashBinding

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private lateinit var prefs: SharedPreferences
    private val binding by viewBinding { FragmentSplashBinding.bind(it) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireActivity().getSharedPreferences("MY_START", Context.MODE_PRIVATE)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        Handler(Looper.getMainLooper()).postDelayed({
            if (prefs.getBoolean("start",false)){
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.mainContainer)
                navController.navigate(R.id.action_splashFragment_to_bottomNav)
            }else{
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.mainContainer)
                navController.navigate(R.id.action_splashFragment_to_startLanguageFragment)
            }
        }, 1000)
    }
}