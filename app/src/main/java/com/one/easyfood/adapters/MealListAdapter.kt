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

class MealListAdapter: RecyclerView.Adapter<MealListAdapter.MealListViewHolder>() {

    private var meals = ArrayList<Meal>()
    private lateinit var context: Context

    fun setMeallist(context: Context, meals: ArrayList<Meal>){
        this.context = context
        this.meals = meals
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealListViewHolder {
        return MealListViewHolder(MealCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MealListViewHolder, position: Int) {
        holder.binding.tvMeal.text = meals[position].strMeal
        Glide.with(holder.itemView).load(meals[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.itemView.setOnClickListener {
            var intent = Intent(context, MealActivity::class.java)
            intent.putExtra("MEAL_ID",meals[position].idMeal)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return meals.count()
    }

    inner class MealListViewHolder(val binding: MealCardBinding): RecyclerView.ViewHolder(binding.root)

}