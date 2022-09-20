package com.bizmiz.moynaktour.ui.home.best_places

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.models.BestPlacesItem
import com.bizmiz.moynaktour.core.models.CategoryItem
import com.bizmiz.moynaktour.core.models.PlacesResponse
import com.bizmiz.moynaktour.core.models.PlacesResponseByDistance
import com.bizmiz.moynaktour.core.utils.Constant
import com.bizmiz.moynaktour.core.utils.getLanguage
import com.bizmiz.moynaktour.databinding.BestPlacesItemBinding
import com.bizmiz.moynaktour.databinding.CategoryItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar

class BestPlacesAdapter : RecyclerView.Adapter<BestPlacesAdapter.ViewHolder>() {
    var bestPlacesList: List<PlacesResponseByDistance> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onclick:(bestPlacesItem: PlacesResponseByDistance)->Unit = {}
    fun onClickListener(onclick:(bestPlacesItem: PlacesResponseByDistance)->Unit){
        this.onclick = onclick
    }
    inner class ViewHolder(private val binding: BestPlacesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val prefs: SharedPreferences = binding.root.context.getSharedPreferences(
            Constant.PREFS,
            Context.MODE_PRIVATE
        )
        private val editor: SharedPreferences.Editor =prefs.edit()
        var id = 3
        fun getCategory(bestPlacesItem: PlacesResponseByDistance, position: Int) {
            Glide.with(binding.root.context)
                .load(bestPlacesItem.imageUrls?.get(0))
                .listener(listener(binding.progressBarItem))
                .into(binding.homeItemImage)
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
            if (prefs.contains(bestPlacesItem.id.toString())){
                binding.btnFavourite.setImageResource(R.drawable.ic_favourite_border)
            }else{
                binding.btnFavourite.setImageResource(R.drawable.ic_favourite)
            }
            binding.btnFavourite.setOnClickListener {
                if (prefs.contains(bestPlacesItem.id.toString())){
                    binding.btnFavourite.setImageResource(R.drawable.ic_favourite)
                    editor.remove(bestPlacesItem.id.toString()).apply()
                    Snackbar.make(binding.root,binding.root.context.getString(R.string.fav), Snackbar.LENGTH_SHORT).show()
                }else{
                    binding.btnFavourite.setImageResource(R.drawable.ic_favourite_border)
                    editor.putBoolean(bestPlacesItem.id.toString(),true).apply()
                    Snackbar.make(binding.root,binding.root.context.getString(R.string.fav_add), Snackbar.LENGTH_SHORT).show()
                }
            }
            binding.homeItemText.text = bestPlacesItem.name!![id]
            binding.itemRating.text = bestPlacesItem.rating
            when(bestPlacesItem.categoryId){
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
                onclick.invoke(bestPlacesItem)
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val homeCategoryItemBinding =
            BestPlacesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(homeCategoryItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCategory(bestPlacesList[position], position)
    }

    override fun getItemCount(): Int = bestPlacesList.size
}