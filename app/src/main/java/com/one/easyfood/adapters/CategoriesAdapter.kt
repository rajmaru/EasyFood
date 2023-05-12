package com.one.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.one.easyfood.databinding.CategoriesListItemBinding
import com.one.easyfood.models.Category

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private var categories= ArrayList<Category>()

    fun setCategoriesList(categories: ArrayList<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            CategoriesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.binding.tvCategory.text = categories[position].strCategory
        Glide.with(holder.itemView)
            .load(categories[position].strCategoryThumb)
            .into(holder.binding.imgCategory)
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

    inner class CategoriesViewHolder(val binding: CategoriesListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}