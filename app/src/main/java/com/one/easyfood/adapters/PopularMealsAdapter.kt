package com.one.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.one.easyfood.databinding.MealCardBinding
import com.one.easyfood.databinding.MealCardRectBinding
import com.one.easyfood.models.Category
import com.one.easyfood.models.Meal

class PopularMealsAdapter : RecyclerView.Adapter<PopularMealsAdapter.PopularMealsViewHolder>() {

    private var popularMealsList= ArrayList<Meal>()

    fun setPopularMealsList(popularMealsList: ArrayList<Meal>) {
        this.popularMealsList = popularMealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealsViewHolder {
        return PopularMealsViewHolder(
            MealCardRectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: PopularMealsViewHolder, position: Int) {
        holder.binding.tvMeal.text = popularMealsList[position].strMeal
        Glide.with(holder.itemView).load(popularMealsList[position].strMealThumb)
            .into(holder.binding.imgMeal)
    }

    override fun getItemCount(): Int {
        return popularMealsList.count()
    }

    inner class PopularMealsViewHolder(val binding: MealCardRectBinding) :
        RecyclerView.ViewHolder(binding.root)

}