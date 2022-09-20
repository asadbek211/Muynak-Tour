package com.bizmiz.moynaktour.ui.settings.main.language

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.utils.LocaleManager
import com.bizmiz.moynaktour.core.utils.viewBinding
import com.bizmiz.moynaktour.databinding.*
import com.orhanobut.hawk.Hawk

class LanguageFragment : Fragment(R.layout.fragment_language) ,View.OnClickListener{
    private lateinit var navController: NavController
    private val binding by viewBinding { FragmentLanguageBinding.bind(it) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.popBackStack()
        }
        binding.english.setOnClickListener(this)
        binding.russian.setOnClickListener(this)
        binding.uzb.setOnClickListener(this)
        binding.kr.setOnClickListener(this)
        when (getLanguage()) {
            "en" -> binding.english.setBackgroundResource(R.drawable.language_shape_selected)
            "ru" -> binding.russian.setBackgroundResource(R.drawable.language_shape_selected)
            "uz" -> binding.uzb.setBackgroundResource(R.drawable.language_shape_selected)
            else -> binding.kr.setBackgroundResource(R.drawable.language_shape_selected)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.english->{setLanguage(1)
                setLanguagePref("en")
            }
            R.id.russian->{setLanguage(2)
                setLanguagePref("ru")
            }
            R.id.uzb->{setLanguage(3)
                setLanguagePref("uz")
            }
            R.id.kr->{setLanguage(4)
                setLanguagePref("")
            }
        }
    }
    private fun setLanguage(id:Int){
        when(id){
            1->{
                binding.english.setBackgroundResource(R.drawable.language_shape_selected)
                binding.russian.setBackgroundResource(R.drawable.language_shape)
                binding.uzb.setBackgroundResource(R.drawable.language_shape)
                binding.kr.setBackgroundResource(R.drawable.language_shape)
            }
            2->{
                binding.russian.setBackgroundResource(R.drawable.language_shape_selected)
                binding.english.setBackgroundResource(R.drawable.language_shape)
                binding.uzb.setBackgroundResource(R.drawable.language_shape)
                binding.kr.setBackgroundResource(R.drawable.language_shape)
            }
            3->{
                binding.uzb.setBackgroundResource(R.drawable.language_shape_selected)
                binding.russian.setBackgroundResource(R.drawable.language_shape)
                binding.english.setBackgroundResource(R.drawable.language_shape)
                binding.kr.setBackgroundResource(R.drawable.language_shape)
            }
            4->{
                binding.kr.setBackgroundResource(R.drawable.language_shape_selected)
                binding.russian.setBackgroundResource(R.drawable.language_shape)
                binding.uzb.setBackgroundResource(R.drawable.language_shape)
                binding.english.setBackgroundResource(R.drawable.language_shape)
            }
        }
    }
    private fun getLanguage(): String {
        return Hawk.get("pref_lang", "")
    }
    private fun setLanguagePref(id: String) {
        Hawk.put("pref_lang", id)
        LocaleManager.setLocale(requireContext())
    }
}