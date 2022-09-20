package com.bizmiz.moynaktour.ui.settings.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.BuildConfig
import com.bizmiz.moynaktour.core.utils.viewBinding
import com.bizmiz.moynaktour.databinding.*

class SettingsMainFragment : Fragment(R.layout.fragment_settings_main) ,View.OnClickListener{
    private lateinit var navController: NavController
    private val binding by viewBinding { FragmentSettingsMainBinding.bind(it) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     binding.language.setOnClickListener(this)
     binding.share.setOnClickListener(this)
     binding.privacyPolicy.setOnClickListener(this)
     binding.about.setOnClickListener(this)
     binding.support.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.language->{
                navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
                navController.navigate(R.id.bottomNav_to_language)
            }
            R.id.share->{
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Tavsiya qilaman: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }
            R.id.privacyPolicy->{
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.freeprivacypolicy.com/live/66044b9a-bde6-4d18-8a5c-2748d47cbec7")
                )
                startActivity(browserIntent)
            }
            R.id.about->{
                navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
                navController.navigate(R.id.bottomNav_to_about)
            }
            R.id.support->{
                supportDialog()
            }
        }
    }
    private fun supportDialog() {
        val dialog = SupportDialog(this)
        dialog.onDevSelected {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Satimov_A"))
            startActivity(browserIntent)
        }
        dialog.onOrganizerSelected {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Satimov_A"))
            startActivity(browserIntent)
        }
    }
}