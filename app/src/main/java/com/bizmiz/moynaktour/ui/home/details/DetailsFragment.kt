package com.bizmiz.moynaktour.ui.home.details

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.models.BestPlacesItem
import com.bizmiz.moynaktour.core.models.PlacesResponse
import com.bizmiz.moynaktour.core.models.PlacesResponseByDistance
import com.bizmiz.moynaktour.core.utils.Constant
import com.bizmiz.moynaktour.core.utils.getLanguage
import com.bizmiz.moynaktour.core.utils.viewBinding
import com.bizmiz.moynaktour.databinding.FragmentDetailsBinding
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng


class DetailsFragment : Fragment(R.layout.fragment_details) {
    private lateinit var detailsAdapter: DetailsAdapter
    private var data:PlacesResponseByDistance? = null
    private val binding by viewBinding { FragmentDetailsBinding.bind(it) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = requireArguments().get("data") as PlacesResponseByDistance?
        detailsAdapter = DetailsAdapter()
        binding.detailsRecView.adapter = detailsAdapter
        binding.btnBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.mainContainer)
            navController.popBackStack()
        }
        if (data!=null){
            val prefs: SharedPreferences = binding.root.context.getSharedPreferences(
                Constant.PREFS,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor =prefs.edit()
            if (prefs.contains(data!!.id.toString())) {
                binding.btnFavourite.setImageResource(R.drawable.ic_favourite_border)
            } else {
                binding.btnFavourite.setImageResource(R.drawable.ic_favourite_black)
            }
            binding.btnFavourite.setOnClickListener {
                if (prefs.contains(data!!.id.toString())) {
                    binding.btnFavourite.setImageResource(R.drawable.ic_favourite_black)
                    editor.remove(data!!.id.toString()).apply()
                } else {
                    binding.btnFavourite.setImageResource(R.drawable.ic_favourite_border)
                    editor.putBoolean(data!!.id.toString(),true).apply()
                }
            }
            Glide.with(binding.root.context)
                .load(data!!.imageUrls?.get(0))
                .into(binding.shapeableImageView)
            binding.itemRating.text = "${data!!.rating} out of 5.0"
            data!!.categoryId?.let { setCategory(it) }
            binding.tvImageMore.text = "+${data!!.imageUrls!!.size-4}"
            detailsAdapter.categoryList = data!!.imageUrls!!
            val distance = data?.distance?.toInt()
            binding.tvDistance.text = "${distance?.toDouble()?.div(1000)} km"
            detailsAdapter.onClickListener {
                Glide.with(binding.root.context)
                    .load(it)
                    .into(binding.shapeableImageView)
            }
            if (data!!.workTime!=null){
                setWorkData(data!!.workTime!!.size, data!!.workTime!!)
            }else{
                binding.check1.visibility = View.GONE
                binding.time1.visibility = View.GONE
                binding.workTime1.visibility = View.GONE
                binding.den1.setText(getString(R.string.work_time))
                binding.linearLayout2.visibility = View.GONE
                binding.linearLayout3.visibility = View.GONE
                binding.linearLayout4.visibility = View.GONE
                binding.linearLayout5.visibility = View.GONE
                binding.linearLayout6.visibility = View.GONE
                binding.linearLayout7.visibility = View.GONE
            }
            when(getLanguage()){
                "uz"-> {
                    setDataLang(2)
                }
                "ru"-> {
                    setDataLang(1)
                }
                "en"-> {
                    setDataLang(0)
                }
                else -> {
                    setDataLang(3)
                }
            }
        }
        binding.btnOpenMap.setOnClickListener {
//            LatLng(43.76572803583452, 59.027634528569465),LatLng(43.76658663899446, 59.028691348845584)
//            val uri =
//                "http://maps.google.com/maps?saddr=" + "43.76572803583452" + "," + "59.027634528569465" + "&daddr=" + "43.76658663899446" + "," + "59.028691348845584"
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
//            startActivity(intent)
//            val navigationIntentUri = Uri.parse("google.navigation:q=" + 12f + "," + 2f)
//            val mapIntent = Intent(Intent.ACTION_VIEW, navigationIntentUri)
//            mapIntent.setPackage("com.google.android.apps.maps")
//            startActivity(mapIntent)
//            val uri =
//                "http://maps.google.com/maps?daddr=" + data?.address
            val uri =
                "http://maps.google.com/maps?daddr=" + "${data?.lat}" + "," + "${data?.long}" + " (" + "Where the party is at" + ")"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }
        binding.detailsRecView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (oldScrollX==0 || oldScrollX>10){
                binding.tvImageMore.visibility = View.VISIBLE
            }else{
                binding.tvImageMore.visibility = View.GONE
            }

        }
    }
    private fun setWorkData(number:Int,list:List<String>){
        when(number){
            5->{
           binding.workTime1.text = list[0]
           binding.workTime2.text = list[1]
           binding.workTime3.text = list[2]
           binding.workTime4.text = list[3]
           binding.workTime5.text = list[4]
           binding.workTime6.visibility = View.GONE
           binding.workTime7.visibility = View.GONE
           binding.time6.visibility = View.GONE
           binding.time7.visibility = View.GONE
           binding.check6.setImageResource(R.drawable.ic_check_no_24)
           binding.check7.setImageResource(R.drawable.ic_check_no_24)
            }
            6->{
                binding.workTime1.text = list[0]
                binding.workTime2.text = list[1]
                binding.workTime3.text = list[2]
                binding.workTime4.text = list[3]
                binding.workTime5.text = list[4]
                binding.workTime6.text = list[5]
                binding.workTime7.visibility = View.GONE
                binding.time7.visibility = View.GONE
                binding.check7.setImageResource(R.drawable.ic_check_no_24)
            }
            7->{
                binding.workTime1.text = list[0]
                binding.workTime2.text = list[1]
                binding.workTime3.text = list[2]
                binding.workTime4.text = list[3]
                binding.workTime5.text = list[4]
                binding.workTime6.text = list[5]
                binding.workTime7.text = list[6]
            }
    }
    }
    private fun setDataLang(id:Int){
        binding.detailsText.text = data?.name!![id]
        if (data?.streetName!=null){
            binding.streetName.text = data?.streetName!![id]
        }else{
            binding.streetName.text = binding.root.context.getString(R.string.malumot_yoq)
        }
        binding.about.text = data?.about!![id]
    }
    private fun setCategory(categoryId:Int){
        when(categoryId){
            1-> {
                binding.placeType.text = binding.root.context.getString(R.string.hotel)
            }
            2-> {
                binding.placeType.text = binding.root.context.getString(R.string.cafe)
            }
            3-> {
                binding.placeType.text = binding.root.context.getString(R.string.emergency)
            }
            4-> {
                binding.placeType.text = binding.root.context.getString(R.string.museum)
            }
            5-> {
                binding.placeType.text = binding.root.context.getString(R.string.shops)
            }
            6-> {
                binding.placeType.text = binding.root.context.getString(R.string.taxi)
            }
            8-> {
                binding.placeType.text = binding.root.context.getString(R.string.zaprafka)
            }
            9-> {
                binding.placeType.text = binding.root.context.getString(R.string.bank)
            }
        }
    }
}