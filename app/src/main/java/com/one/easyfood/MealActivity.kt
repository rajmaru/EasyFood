package com.one.easyfood

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.one.easyfood.adapters.IngredientsAdapter
import com.one.easyfood.databinding.ActivityMealBinding
import com.one.easyfood.models.Ingredients
import com.one.easyfood.models.Meal
import com.one.easyfood.models.MealsYoutubeLinks
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory


class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var youtubeLink: String
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private var mealId: String? = null
    private var meal: Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MealsViewModelFactory(this))[MealsViewModel::class.java]
        ingredientsAdapter = IngredientsAdapter()

        getMealId()
        onClick()

    }

    private fun getMealId() {
        mealId = intent.getStringExtra("MEAL_ID").toString()
        getMealDataById()
    }

    private fun getMealDataById() {
        viewModel.getMealById(mealId!!).observe(this, Observer { mealResponse ->
            if (mealResponse != null) {
                meal = mealResponse
                if (!meal!!.strYoutube.isNullOrEmpty()) {
                    youtubeLink = meal!!.strYoutube.toString()
                } else {
                    val str = Regex("[^A-Za-z0-9]").replace(meal!!.strMeal.toString(), "")
                    if (MealsYoutubeLinks.isInEnum(str)) {
                        youtubeLink = MealsYoutubeLinks.valueOf(str).strYoutube
                    }
                }
                setDataInViews()
                getIngredientsList()
            }
        })
    }

    private fun setDataInViews() {


        // The cross-fade transition
        val factory = DrawableCrossFadeFactory.Builder()
            .setCrossFadeEnabled(true)
            .build()


        binding.tvMealName.text = meal!!.strMeal
        Glide.with(this@MealActivity)
            .load(meal!!.strMealThumb)
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .into(binding.imgMeal)
        if(meal!!.strInstructions?.elementAt(0) == '.'){
            Log.d("REMOVE_DOT", meal!!.strInstructions!!.elementAt(0).toString())
            meal!!.strInstructions = meal!!.strInstructions!!.addCharAtIndex('1',0)
        }
        if(!meal!!.strInstructions?.contains("\r\n\r\n")!!){
            meal!!.strInstructions = meal!!.strInstructions?.replace(".\r\n", "\r\n\r\n")
        }
        meal!!.strInstructions = meal!!.strInstructions?.trim()
        binding.tvInstructions.text = meal!!.strInstructions
    }

    private fun String.addCharAtIndex(char: Char, index: Int) =
        StringBuilder(this).apply { insert(index, char) }.toString()

    private fun getIngredientsList() {
        val ingredientsList = ArrayList<Ingredients>()
        ingredientsList.apply {
            add(Ingredients(meal!!.strIngredient1, meal!!.strMeasure1))
            add(Ingredients(meal!!.strIngredient2, meal!!.strMeasure2))
            add(Ingredients(meal!!.strIngredient3, meal!!.strMeasure3))
            add(Ingredients(meal!!.strIngredient4, meal!!.strMeasure4))
            add(Ingredients(meal!!.strIngredient5, meal!!.strMeasure5))
            add(Ingredients(meal!!.strIngredient6, meal!!.strMeasure6))
            add(Ingredients(meal!!.strIngredient7, meal!!.strMeasure7))
            add(Ingredients(meal!!.strIngredient8, meal!!.strMeasure8))
            add(Ingredients(meal!!.strIngredient9, meal!!.strMeasure9))
            add(Ingredients(meal!!.strIngredient10, meal!!.strMeasure10))
            add(Ingredients(meal!!.strIngredient11, meal!!.strMeasure11))
            add(Ingredients(meal!!.strIngredient12, meal!!.strMeasure12))
            add(Ingredients(meal!!.strIngredient13, meal!!.strMeasure13))
            add(Ingredients(meal!!.strIngredient14, meal!!.strMeasure14))
            add(Ingredients(meal!!.strIngredient15, meal!!.strMeasure15))
            add(Ingredients(meal!!.strIngredient16, meal!!.strMeasure16))
            add(Ingredients(meal!!.strIngredient17, meal!!.strMeasure17))
            add(Ingredients(meal!!.strIngredient18, meal!!.strMeasure18))
            add(Ingredients(meal!!.strIngredient19, meal!!.strMeasure19))
            add(Ingredients(meal!!.strIngredient20, meal!!.strMeasure20))

        }
        val itr = ingredientsList.iterator()
        while (itr.hasNext()) {
            val curr = itr.next()
            if (curr.name.isNullOrBlank() && curr.size.isNullOrBlank()) {
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
        binding.mealFavoriteBtn.setOnClickListener{
            viewModel.saveMeal(meal!!)
        }

        binding.btnYoutube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink)))
        }
    }
}