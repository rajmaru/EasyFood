package com.one.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.one.easyfood.databinding.HomeCategoriesChipBinding
import com.one.easyfood.models.Category


class CategoriesChipAdapter: RecyclerView.Adapter<CategoriesChipAdapter.CategoriesChipViewHolder>() {

    private var categories= ArrayList<Category>()
    fun setCategoryList(categories: ArrayList<Category>){
        this.categories = categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesChipViewHolder {
        return CategoriesChipViewHolder(HomeCategoriesChipBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoriesChipViewHolder, position: Int) {
        holder.binding.tvCategoryChip.text = categories[position].strCategory
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

    inner class CategoriesChipViewHolder(val binding: HomeCategoriesChipBinding): RecyclerView.ViewHolder(binding.root)

}