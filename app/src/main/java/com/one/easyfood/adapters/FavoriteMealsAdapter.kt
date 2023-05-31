package com.one.easyfood.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.one.easyfood.MealActivity
import com.one.easyfood.databinding.MealCardBinding
import com.one.easyfood.models.Meal

class FavoriteMealsAdapter : RecyclerView.Adapter<FavoriteMealsAdapter.FavoriteMealsViewHolder>(){

    private lateinit var favMealsList: List<Meal>
    private lateinit var context: Context

    fun setFavMealsList(context: Context, favMealsList: List<Meal>){
        this.context = context
        this.favMealsList = favMealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealsViewHolder {
        return FavoriteMealsViewHolder(
            MealCardBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteMealsViewHolder, position: Int) {
        val factory = DrawableCrossFadeFactory.Builder()
            .setCrossFadeEnabled(true)
            .build()
        holder.binding.tvMeal.text = favMealsList[position].strMeal
        Glide.with(holder.itemView).load(favMealsList[position].strMealThumb)
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .into(holder.binding.imgMeal)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra("MEAL_ID",favMealsList[position].idMeal)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return favMealsList.size
    }

    inner class FavoriteMealsViewHolder(val binding: MealCardBinding): RecyclerView.ViewHolder(binding.root)

}