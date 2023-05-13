package com.one.easyfood.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.one.easyfood.MealActivity
import com.one.easyfood.databinding.MealCardRectBinding
import com.one.easyfood.models.Meal

class PopularMealsAdapter : RecyclerView.Adapter<PopularMealsAdapter.PopularMealsViewHolder>() {

    private var popularMealsList= ArrayList<Meal>()
    private lateinit var context: Context

    fun setPopularMealsList(context: Context, popularMealsList: ArrayList<Meal>) {
        this.context = context
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
        holder.itemView.setOnClickListener {
            var intent = Intent(context, MealActivity::class.java)
            intent.putExtra("MEAL_ID",popularMealsList[position].idMeal)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return popularMealsList.count()
    }

    inner class PopularMealsViewHolder(val binding: MealCardRectBinding) :
        RecyclerView.ViewHolder(binding.root)
}