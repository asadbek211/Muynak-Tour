package com.bizmiz.moynaktour.ui.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.moynaktour.core.models.CategoryItem
import com.bizmiz.moynaktour.databinding.CategoryItemBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    var categoryList: ArrayList<CategoryItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onclick:(categoryId: Int)->Unit = {}
    fun onClickListener(onclick:(categoryId: Int)->Unit){
        this.onclick = onclick
    }
    inner class ViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun getCategory(categoryItem: CategoryItem, position: Int) {
            binding.homeItemCategoryImage.setImageResource(categoryItem.ivImage)
            binding.homeItemCategoryText.text = categoryItem.tvName
            binding.homeItemCategoryImage.setOnClickListener {
                onclick.invoke(position+1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val homeCategoryItemBinding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(homeCategoryItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCategory(categoryList[position], position)
    }

    override fun getItemCount(): Int = categoryList.size
}