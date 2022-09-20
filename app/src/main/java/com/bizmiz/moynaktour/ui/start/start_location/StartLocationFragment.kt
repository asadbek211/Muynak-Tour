package com.bizmiz.moynaktour.ui.start.start_location

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.utils.LocaleManager
import com.bizmiz.moynaktour.databinding.FragmentStartLocationBinding

class StartLocationFragment : Fragment() {
    private lateinit var binding: FragmentStartLocationBinding
    private lateinit var prefs: SharedPreferences
    private var lang: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        prefs = requireActivity().getSharedPreferences("MY_START", Context.MODE_PRIVATE)
        lang = requireArguments().getInt("number", 0)
        binding = FragmentStartLocationBinding.bind(
            inflater.inflate(
                R.layout.fragment_start_location,
                container,
                false
            )
        )
        requestPermission()
        setText(lang)
        binding.btnNext.setOnClickListener {
            if (checkPermission()){
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.mainContainer)
                navController.navigate(R.id.action_startLocationFragment_to_bottomNav)
                LocaleManager.setLocale(requireContext())
                prefs.edit().putBoolean("start",true).apply()
            }else{
                requestPermission()
            }
        }
        return binding.root
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 1010
        )
    }

    private fun setText(lang: Int) {
        when (lang) {
            0 -> {
                binding.tvTitle.text =
                    "To calculate the places near you, you need to know your location"
                binding.btnNext.text = "Positioning"
            }
            1 -> {
                binding.tvTitle.text =
                "Чтобы рассчитать места рядом с вами, вам нужно знать свое местоположение"
                binding.btnNext.text = "Позиционирование"
            }
            2 -> {
                binding.tvTitle.text =
                    "Sizga yaqin bo'lgan joylarni hisoblash uchun joylashuvingizni aniqlash lozim"
                binding.btnNext.text = "Joylashuvni aniqlash"
            }
            3 -> {
                binding.tvTitle.text =
                    "Sizge jaqın bolǵan jaylardı esaplaw ushın jaylasıwıńızdı anıqlaw kerek"
                binding. btnNext. text = " Jaylasıwdı anıqlaw"
            }
        }
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
}