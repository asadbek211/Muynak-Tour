package com.bizmiz.moynaktour.ui.home.details

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.moynaktour.core.models.CategoryItem
import com.bizmiz.moynaktour.databinding.CategoryItemBinding
import com.bizmiz.moynaktour.databinding.DetailsImageItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class DetailsAdapter : RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {
    var categoryList: List<String> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onclick:(imageUrl: String)->Unit = {}
    fun onClickListener(onclick:(imageUrl: String)->Unit){
        this.onclick = onclick
    }
    inner class ViewHolder(private val binding: DetailsImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun getCategory(position: Int) {
            Glide.with(binding.root.context)
                .load(categoryList[position])
                .listener(listener(binding.progressBarItem))
                .into(binding.ivImage)
            binding.ivImage.setOnClickListener {
                onclick.invoke(categoryList[position])
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
            DetailsImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(homeCategoryItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCategory(position)
    }

    override fun getItemCount(): Int = categoryList.size
}