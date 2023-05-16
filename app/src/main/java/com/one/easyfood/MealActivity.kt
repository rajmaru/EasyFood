package com.one.easyfood

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.hardware.biometrics.BiometricManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.StringBuilderPrinter
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.one.easyfood.adapters.IngredientsAdapter
import com.one.easyfood.databinding.ActivityMealBinding
import com.one.easyfood.models.Ingredients
import com.one.easyfood.models.Meal
import com.one.easyfood.viewmodel.ApiViewModel
import java.util.Collections
import java.util.Objects


class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var viewModel: ApiViewModel
    private lateinit var youtubeLink: String
    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ApiViewModel::class.java]
        ingredientsAdapter = IngredientsAdapter()

        getMealId()
        onClick()

    }

    private fun getMealId() {
        val mealId = intent.getStringExtra("MEAL_ID").toString()
        getMealDataById(mealId)
    }

    private fun getMealDataById(mealId: String) {
        viewModel.getMealById(mealId).observe(this, Observer { mealResponse ->
            if (mealResponse != null) {
                youtubeLink = mealResponse.strYoutube
                setDataInViews(mealResponse)
                getIngredientsList(mealResponse)
            }
        })
    }

    private fun setDataInViews(meal: Meal) {
        binding.tvMealName.text = meal.strMeal
        Glide.with(this@MealActivity)
            .load(meal.strMealThumb)
            .into(binding.imgMeal)
        binding.tvInstructions.text = meal.strInstructions
    }

    private fun getIngredientsList(meal: Meal) {
        val ingredientsList = ArrayList<Ingredients>()
        ingredientsList.apply {
            add(Ingredients(meal.strIngredient1, meal.strMeasure1))
            add(Ingredients(meal.strIngredient2, meal.strMeasure2))
            add(Ingredients(meal.strIngredient3, meal.strMeasure3))
            add(Ingredients(meal.strIngredient4, meal.strMeasure4))
            add(Ingredients(meal.strIngredient5, meal.strMeasure5))
            add(Ingredients(meal.strIngredient6, meal.strMeasure6))
            add(Ingredients(meal.strIngredient7, meal.strMeasure7))
            add(Ingredients(meal.strIngredient8, meal.strMeasure8))
            add(Ingredients(meal.strIngredient9, meal.strMeasure9))
            add(Ingredients(meal.strIngredient10, meal.strMeasure10))
            add(Ingredients(meal.strIngredient11, meal.strMeasure11))
            add(Ingredients(meal.strIngredient12, meal.strMeasure12))
            add(Ingredients(meal.strIngredient13, meal.strMeasure13))
            add(Ingredients(meal.strIngredient14, meal.strMeasure14))
            add(Ingredients(meal.strIngredient15, meal.strMeasure15))
            add(Ingredients(meal.strIngredient16, meal.strMeasure16))
            add(Ingredients(meal.strIngredient17, meal.strMeasure17))
            add(Ingredients(meal.strIngredient18, meal.strMeasure18))
            add(Ingredients(meal.strIngredient19, meal.strMeasure19))
            add(Ingredients(meal.strIngredient20, meal.strMeasure20))

        }
        val itr = ingredientsList.iterator()
        while(itr.hasNext()){
            val curr = itr.next()
            if(curr.name.isNullOrBlank() && curr.size.isNullOrBlank()){
                itr.remove()
            }
        }
        setIngredientsRV(ingredientsList)
    }

    private fun setIngredientsRV(ingredientsList: ArrayList<Ingredients>) {
        ingredientsAdapter.setIngredientsList(ingredientsList)
        binding.rvIngredients.apply {
            adapter = ingredientsAdapter
            layoutManager =
                LinearLayoutManager(this@MealActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun onClick() {
        binding.mealBackBtn.setOnClickListener {
            finish()
        }

        binding.btnYoutube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink)))
        }
    }
}