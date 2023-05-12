package com.one.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.one.easyfood.databinding.MealCardBinding
import com.one.easyfood.databinding.MealCardRectBinding
import com.one.easyfood.models.Meal

class RecommendedAdapter: RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder>() {

    private var recommendedList= ArrayList<Meal>()

    fun setRecommendedList(recommendedList: ArrayList<Meal>) {
        this.recommendedList = recommendedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        return RecommendedViewHolder(
            MealCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        holder.binding.tvMeal.text = recommendedList[position].strMeal
        Glide.with(holder.itemView).load(recommendedList[position].strMealThumb)
            .into(holder.binding.imgMeal)
    }

    override fun getItemCount(): Int {
        return recommendedList.count()
    }

    inner class RecommendedViewHolder(val binding: MealCardBinding) :
        RecyclerView.ViewHolder(binding.root)

}