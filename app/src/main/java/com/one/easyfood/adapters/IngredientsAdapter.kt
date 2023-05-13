package com.one.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.one.easyfood.databinding.IngredientsChipBinding
import com.one.easyfood.models.Ingredients

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    private var ingredients= ArrayList<Ingredients>()

    fun setIngredientsList(ingredients: ArrayList<Ingredients>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        return IngredientsViewHolder(IngredientsChipBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.binding.tvIngredientsName.text = ingredients[position].name
        holder.binding.tvIngredientsSize.text = ingredients[position].size
    }

    override fun getItemCount(): Int {
        return ingredients.count()
    }

    inner class IngredientsViewHolder(val binding: IngredientsChipBinding) :
        RecyclerView.ViewHolder(binding.root)

}