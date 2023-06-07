package com.one.easyfood.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.one.easyfood.MealListActivity
import com.one.easyfood.databinding.CategoriesListItemBinding
import com.one.easyfood.models.Category

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private lateinit var context: Context
    private var categories = ArrayList<Category>()

    fun setCategoriesList(context: Context, categories: ArrayList<Category>) {
        this.context = context
        this.categories = categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            CategoriesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        // The cross-fade transition
        val factory = DrawableCrossFadeFactory.Builder()
            .setCrossFadeEnabled(true)
            .build()
        holder.binding.tvCategory.text = categories[position].strCategory
        Glide.with(holder.itemView)
            .load(categories[position].strCategoryThumb)
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .into(holder.binding.imgCategory)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MealListActivity::class.java)
            intent.putExtra("CATEGORY_NAME", categories[position].strCategory)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoriesViewHolder(val binding: CategoriesListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}