package com.one.easyfood.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.one.easyfood.MealActivity
import com.one.easyfood.databinding.MealCardBinding
import com.one.easyfood.models.Meal

class SearchedMealAdapter : RecyclerView.Adapter<SearchedMealAdapter.SearchedMealViewHolder>() {
    private var searchedMeals = ArrayList<Meal>()
    private lateinit var context: Context
    private var isConnected: Boolean = false

    fun setSearchedMealList(context: Context, searchedMeals: ArrayList<Meal>){
        this.searchedMeals = searchedMeals
        this.context = context
        notifyDataSetChanged()
    }

    fun setIsConnected(isConnected: Boolean){
        this.isConnected = isConnected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedMealViewHolder {
        return SearchedMealViewHolder(MealCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SearchedMealViewHolder, position: Int) {
        val factory = DrawableCrossFadeFactory.Builder()
            .setCrossFadeEnabled(true)
            .build()
        holder.binding.tvMeal.text = searchedMeals[position].strMeal
        Glide.with(holder.itemView).load(searchedMeals[position].strMealThumb)
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .into(holder.binding.imgMeal)
        holder.itemView.setOnClickListener {
            if(isConnected){
                val intent = Intent(context, MealActivity::class.java)
                intent.putExtra("MEAL_ID", searchedMeals[position].idMeal)
                context.startActivity(intent)
            }else{
                Toast.makeText(this.context, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return searchedMeals.size
    }


    inner class SearchedMealViewHolder(val binding: MealCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}