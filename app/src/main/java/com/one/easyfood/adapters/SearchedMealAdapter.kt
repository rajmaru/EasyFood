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

class SearchedMealAdapter : RecyclerView.Adapter<SearchedMealAdapter.SearchedMealViewHolder>() {
    private var searchedMeals = ArrayList<Meal>()
    private lateinit var context: Context

    fun setSearchedMealList(context: Context, searchedMeals: ArrayList<Meal>){
        this.searchedMeals = searchedMeals
        this.context = context
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedMealViewHolder {
        return SearchedMealViewHolder(MealCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SearchedMealViewHolder, position: Int) {
        holder.binding.tvMeal.text = searchedMeals[position].strMeal
        Glide.with(holder.itemView).load(searchedMeals[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra("MEAL_ID", searchedMeals[position].idMeal)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return searchedMeals.size
    }


    inner class SearchedMealViewHolder(val binding: MealCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}