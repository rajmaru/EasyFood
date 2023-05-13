package com.one.easyfood.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.one.easyfood.MealActivity
import com.one.easyfood.databinding.MealCardBinding
import com.one.easyfood.models.Meal

class RecommendedAdapter: RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder>() {

    private var recommendedList= ArrayList<Meal>()
    private lateinit var context: Context

    fun setRecommendedList(context: Context, recommendedList: ArrayList<Meal>) {
        this.context = context
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
        holder.itemView.setOnClickListener {
            var intent = Intent(context, MealActivity::class.java)
            intent.putExtra("MEAL_ID", recommendedList[position].idMeal)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recommendedList.count()
    }

    inner class RecommendedViewHolder(val binding: MealCardBinding) :
        RecyclerView.ViewHolder(binding.root)

}