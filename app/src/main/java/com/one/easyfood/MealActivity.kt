package com.one.easyfood

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
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


class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var viewModel: ApiViewModel
    private var ingredientsList = ArrayList<Ingredients>()
    private lateinit var meal: Meal
    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        getWindow().setStatusBarColor(Color.TRANSPARENT)
        // this lines ensure only the status-bar to become transparent without affecting the nav-bar
        // this lines ensure only the status-bar to become transparent without affecting the nav-bar
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        viewModel = ViewModelProvider(this)[ApiViewModel::class.java]
        ingredientsAdapter = IngredientsAdapter()

        getMealId()
        setIngredientsRV()
        onClick()

    }

    private fun getMealId() {
        mealId = intent.getStringExtra("MEAL_ID").toString()
        getMealDataById()
    }

    private fun getMealDataById() {
        viewModel.getMealById(mealId).observe(this, Observer { mealResponse ->
            if (mealResponse != null) {
                meal = mealResponse
                setDataInViews()
                getIngredientsList(mealResponse)
            }
        })
    }

    private fun setDataInViews() {
        binding.tvMealName.text = meal.strMeal
        Glide.with(this@MealActivity)
            .load(meal.strMealThumb)
            .into(binding.imgMeal)
        binding.tvInstructions.text = meal.strInstructions
    }

    private fun getIngredientsList(meal: Meal) {
        ingredientsList.apply {
            Ingredients(meal.strIngredient1, meal.strMeasure1)
            Ingredients(meal.strIngredient2, meal.strMeasure2)
            Ingredients(meal.strIngredient3, meal.strMeasure3)
            Ingredients(meal.strIngredient4, meal.strMeasure4)
            Ingredients(meal.strIngredient5, meal.strMeasure5)
            Ingredients(meal.strIngredient6, meal.strMeasure6)
            Ingredients(meal.strIngredient7, meal.strMeasure7)
            Ingredients(meal.strIngredient8, meal.strMeasure8)
            Ingredients(meal.strIngredient9, meal.strMeasure9)
            Ingredients(meal.strIngredient10, meal.strMeasure10)
            Ingredients(meal.strIngredient11, meal.strMeasure11)
            Ingredients(meal.strIngredient12, meal.strMeasure12)
            Ingredients(meal.strIngredient13, meal.strMeasure13)
            Ingredients(meal.strIngredient14, meal.strMeasure14)
            Ingredients(meal.strIngredient15, meal.strMeasure15)
            Ingredients(meal.strIngredient16, meal.strMeasure16)
            Ingredients(meal.strIngredient17, meal.strMeasure17)
            Ingredients(meal.strIngredient18, meal.strMeasure18)
            Ingredients(meal.strIngredient19, meal.strMeasure19)
            Ingredients(meal.strIngredient20, meal.strMeasure20)
        }
    }

    private fun setIngredientsRV() {
        ingredientsAdapter.setIngredientsList(ingredientsList)
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvIngredients.setHasFixedSize(true)
    }

    private fun onClick() {
        binding.imgBackArrow.setOnClickListener {
            finish()
        }

        binding.btnYoutube.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(meal.strYoutube)))
        }
    }
}