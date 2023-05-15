package com.one.easyfood.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.one.easyfood.MealListActivity
import com.one.easyfood.databinding.HomeCategoriesChipBinding
import com.one.easyfood.models.Category


class CategoriesChipAdapter: RecyclerView.Adapter<CategoriesChipAdapter.CategoriesChipViewHolder>() {

    private lateinit var context: Context
    private var categories= ArrayList<Category>()
    fun setCategoryList(context: Context, categories: ArrayList<Category>){
        this.context = context
        this.categories = categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesChipViewHolder {
        return CategoriesChipViewHolder(HomeCategoriesChipBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoriesChipViewHolder, position: Int) {
        holder.binding.tvCategoryChip.text = categories[position].strCategory
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MealListActivity::class.java)
            intent.putExtra("CATEGORY_NAME", categories[position].strCategory)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

    inner class CategoriesChipViewHolder(val binding: HomeCategoriesChipBinding): RecyclerView.ViewHolder(binding.root)

}