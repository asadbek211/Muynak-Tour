package com.bizmiz.moynaktour.ui.favourite

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.models.*
import com.bizmiz.moynaktour.core.utils.Constant
import com.bizmiz.moynaktour.core.utils.getDistance
import com.bizmiz.moynaktour.core.utils.getLanguage
import com.bizmiz.moynaktour.databinding.BestPlacesItemBinding
import com.bizmiz.moynaktour.databinding.CategoryItemBinding
import com.bizmiz.moynaktour.databinding.NearbyPlacesItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.model.LatLng

class FavNearbyAdapter : RecyclerView.Adapter<FavNearbyAdapter.ViewHolder>() {
    var nearbyPlacesList: List<PlacesResponseByDistance> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onclick:(nearbyPlacesItem: PlacesResponseByDistance)->Unit = {}
    fun onClickListener(onclick:(nearbyPlacesItem: PlacesResponseByDistance)->Unit){
        this.onclick = onclick
    }
    inner class ViewHolder(private val binding: NearbyPlacesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val prefs: SharedPreferences = binding.root.context.getSharedPreferences(
            Constant.PREFS,
            Context.MODE_PRIVATE
        )
        private val editor: SharedPreferences.Editor =prefs.edit()
        var id = 3
        fun getCategory(nearbyPlacesItem: PlacesResponseByDistance, position: Int) {
            Glide.with(binding.root.context)
                .load(nearbyPlacesItem.imageUrls?.get(0))
                .listener(listener(binding.progressBarItem))
                .into(binding.itemImage)
            when(getLanguage()){
                "uz"-> {
                    id = 2
                }
                "ru"-> {
                    id = 1
                }
                "en"-> {
                    id = 0
                }
                else -> {
                    id = 3
                }
            }
            if (prefs.contains(nearbyPlacesItem.id.toString())){
                binding.homeInfoBookmark.setImageResource(R.drawable.ic_favourite_border)
            }else{
                binding.homeInfoBookmark.setImageResource(R.drawable.ic_favourite_white)
            }
            binding.homeInfoBookmark.setOnClickListener {
                if (prefs.contains(nearbyPlacesItem.id.toString())){
                    binding.homeInfoBookmark.setImageResource(R.drawable.ic_favourite_white)
                    saralash.invoke(true, nearbyPlacesItem.id.toString(),editor)
                }else{
                    binding.homeInfoBookmark.setImageResource(R.drawable.ic_favourite_border)
                    saralash.invoke(false, nearbyPlacesItem.id.toString(),editor)
                }
            }
             val distance = nearbyPlacesItem.distance?.toInt()
            binding.tvDistance.text = "${distance?.toDouble()?.div(1000)} km"
            if (nearbyPlacesItem.streetName!=null){
                binding.streetName.text = nearbyPlacesItem.streetName[id]
            }else{
                binding.streetName.text = binding.root.context.getString(R.string.malumot_yoq)
            }
            binding.homeNearbyName.text = nearbyPlacesItem.name!![id]
            binding.tvRating.text = nearbyPlacesItem.rating
            when(nearbyPlacesItem.categoryId){
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
            binding.containerLay.setOnClickListener {
                onclick.invoke(nearbyPlacesItem)
            }
        }
    }
    private fun listener(progressBar: ProgressBar) = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            progressBar.isVisible = false
            return false
        }

    }
    var saralash: (key:Boolean,id: String, editor: SharedPreferences.Editor) -> Unit =
        { key:Boolean,id: String, editor: SharedPreferences.Editor -> }

    fun favListener(saralash: (key:Boolean,id: String, editor: SharedPreferences.Editor) -> Unit) {
        this.saralash = saralash
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val homeCategoryItemBinding =
            NearbyPlacesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(homeCategoryItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCategory(nearbyPlacesList[position], position)
    }

    override fun getItemCount(): Int = nearbyPlacesList.size
}