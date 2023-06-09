package com.one.easyfood.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.one.easyfood.activities.MealActivity
import com.one.easyfood.databinding.MealCardBinding
import com.one.easyfood.models.Meal

class FavoritesListAdapter : RecyclerView.Adapter<FavoritesListAdapter.FavoritesListViewHolder>() {

    private var meals = ArrayList<Meal>()
    private lateinit var context: Context

    fun setMeallist(context: Context, meals: ArrayList<Meal>) {
        this.context = context
        this.meals = meals
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesListViewHolder {
        return FavoritesListViewHolder(
            MealCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoritesListViewHolder, position: Int) {
        val factory = DrawableCrossFadeFactory.Builder()
            .setCrossFadeEnabled(true)
            .build()
        holder.binding.tvMeal.text = meals[position].strMeal
        Glide.with(holder.itemView).load(meals[position].strMealThumb)
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .into(holder.binding.imgMeal)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra("MEAL_ID", meals[position].idMeal)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    inner class FavoritesListViewHolder(val binding: MealCardBinding) :
        RecyclerView.ViewHolder(binding.root)

}