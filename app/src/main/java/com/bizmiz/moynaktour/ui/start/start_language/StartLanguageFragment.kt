package com.bizmiz.moynaktour.ui.start.start_language

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.databinding.FragmentStartLanguageBinding
import com.orhanobut.hawk.Hawk

class StartLanguageFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentStartLanguageBinding
    private var lang = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        binding = FragmentStartLanguageBinding.bind(
            inflater.inflate(
                R.layout.fragment_start_language,
                container,
                false
            )
        )
        binding.english.setOnClickListener(this)
        binding.russian.setOnClickListener(this)
        binding.uzb.setOnClickListener(this)
        binding.qr.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        when (lang) {
            0 -> {
                binding.english.setBackgroundResource(R.drawable.shape_stroke)
                binding.russian.setBackgroundResource(R.drawable.settings_selector)
                binding.uzb.setBackgroundResource(R.drawable.settings_selector)
                binding.qr.setBackgroundResource(R.drawable.settings_selector)
                binding.tvTitle.text = "Choose a language"
                binding.btnNext.text = "Next"
            }
            1 -> {
                binding.russian.setBackgroundResource(R.drawable.shape_stroke)
                binding.english.setBackgroundResource(R.drawable.settings_selector)
                binding.uzb.setBackgroundResource(R.drawable.settings_selector)
                binding.qr.setBackgroundResource(R.drawable.settings_selector)
                binding.tvTitle.text = "Выберите язык"
                binding.btnNext.text = "Далее"
            }
            2 -> {
                binding.uzb.setBackgroundResource(R.drawable.shape_stroke)
                binding.english.setBackgroundResource(R.drawable.settings_selector)
                binding.russian.setBackgroundResource(R.drawable.settings_selector)
                binding.qr.setBackgroundResource(R.drawable.settings_selector)
                binding.tvTitle.text = "Tilni tanlang"
                binding.btnNext.text = "Keyingi"
            }
            3 -> {
                binding.qr.setBackgroundResource(R.drawable.shape_stroke)
                binding.english.setBackgroundResource(R.drawable.settings_selector)
                binding.russian.setBackgroundResource(R.drawable.settings_selector)
                binding.uzb.setBackgroundResource(R.drawable.settings_selector)
                binding.tvTitle.text = "Tildi saylań"
                binding.btnNext.text = "Keyingi"
            }
        }
        return binding.root
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.english -> {
                binding.english.setBackgroundResource(R.drawable.shape_stroke)
                binding.russian.setBackgroundResource(R.drawable.settings_selector)
                binding.uzb.setBackgroundResource(R.drawable.settings_selector)
                binding.qr.setBackgroundResource(R.drawable.settings_selector)
                binding.tvTitle.text = "Choose a language"
                binding.btnNext.text = "Next"
                lang = 0
            }
            R.id.russian -> {
                binding.russian.setBackgroundResource(R.drawable.shape_stroke)
                binding.english.setBackgroundResource(R.drawable.settings_selector)
                binding.uzb.setBackgroundResource(R.drawable.settings_selector)
                binding.qr.setBackgroundResource(R.drawable.settings_selector)
                binding.tvTitle.text = "Выберите язык"
                binding.btnNext.text = "Далее"
                lang = 1
            }
            R.id.uzb -> {
                binding.uzb.setBackgroundResource(R.drawable.shape_stroke)
                binding.english.setBackgroundResource(R.drawable.settings_selector)
                binding.russian.setBackgroundResource(R.drawable.settings_selector)
                binding.qr.setBackgroundResource(R.drawable.settings_selector)
                binding.tvTitle.text = "Tilni tanlang"
                binding.btnNext.text = "Keyingi"
                lang = 2
            }
            R.id.qr -> {
                binding.qr.setBackgroundResource(R.drawable.shape_stroke)
                binding.english.setBackgroundResource(R.drawable.settings_selector)
                binding.russian.setBackgroundResource(R.drawable.settings_selector)
                binding.uzb.setBackgroundResource(R.drawable.settings_selector)
                binding.tvTitle.text = "Tildi saylań"
                binding.btnNext.text = "Keyingi"
                lang = 3
            }
            R.id.btnNext -> {
                when (lang) {
                    0 -> {
                        setLanguage("en")
                    }
                    1 -> {
                        setLanguage("ru")
                    }
                    2 -> {
                        setLanguage("uz")
                    }
                    3 -> {
                        setLanguage("")
                    }
                }
            }
        }
    }

    private fun setLanguage(id: String) {
        Hawk.put("pref_lang", id)
        val bundle = bundleOf(
            "number" to lang
        )
        val navController =
            Navigation.findNavController(requireActivity(), R.id.mainContainer)
        navController.navigate(R.id.action_startLanguageFragment_to_startLocationFragment, bundle)
    }
}