package com.one.easyfood.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import com.one.easyfood.R
import com.one.easyfood.adapters.IngredientsAdapter
import com.one.easyfood.databinding.ActivityMealBinding
import com.one.easyfood.itemdecoration.IngredientsItemMargin
import com.one.easyfood.models.Ingredients
import com.one.easyfood.models.Meal
import com.one.easyfood.models.MealsYoutubeLinks
import com.one.easyfood.networkconnection.NetworkConnection
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory


class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var viewModel: MealsViewModel
    private var youtubeLink: String? = null
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private var mealId: String? = null
    private var meal: Meal? = null
    private lateinit var customItemMargin: IngredientsItemMargin
    private lateinit var networkConnection: NetworkConnection
    private var isConnected: Boolean = false
    private lateinit var snackbar: Snackbar
    private lateinit var slideUpAnimation: Animation
    private lateinit var slideDownAnimation: Animation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        checkInternetConnection()
        onClick()
    }

    private fun init() {
        viewModel = ViewModelProvider(this, MealsViewModelFactory(this))[MealsViewModel::class.java]
        snackbar = Snackbar.make(binding.mealSnackbarLayout, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
            .setAnchorView(binding.mealSnackbarLayout)
            .setBackgroundTint(resources.getColor(R.color.theme))
            .setTextColor(resources.getColor(R.color.snackbar_text))
            .setAnimationMode(ANIMATION_MODE_SLIDE)
            .setAction("Cancel"){
                snackbar.dismiss()
            }
            .setActionTextColor(resources.getColor(R.color.snackbar_text))
        ingredientsAdapter = IngredientsAdapter()
        customItemMargin = IngredientsItemMargin()
        slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down)
    }

    private fun checkInternetConnection() {
        networkConnection = NetworkConnection(this)
        networkConnection.observe(this) { isConnected ->
            this.isConnected = isConnected
            if (isConnected) {
                if(snackbar.isShown){
                    snackbar.dismiss()
                    binding.btnYoutube.startAnimation(slideUpAnimation)
                    binding.btnYoutube.visibility = View.VISIBLE
                }
                getMealId()
            } else {
                if(!snackbar.isShown){
                    binding.btnYoutube.startAnimation(slideDownAnimation)
                    binding.btnYoutube.visibility = View.GONE
                    snackbar.show()
                }
            }
        }
    }

    private fun onClick() {
        binding.mealBackBtn.setOnClickListener {
            finish()
        }
        binding.mealFavoriteBtn.setOnClickListener {
            viewModel.isMealFavorite(mealId).observe(this) { isFavorite ->
                if (isFavorite == true) {
                    viewModel.deleteMeal(meal!!)
                    binding.mealFavoriteBtn.setImageResource(R.drawable.ic_favorite)
                } else {
                    viewModel.saveMeal(meal!!)
                    binding.mealFavoriteBtn.setImageResource(R.drawable.ic_favorite_filled)
                }
            }
        }


        binding.btnYoutube.setOnClickListener {
            if(isConnected){
                val intent = Intent(this, VideoView::class.java)
                intent.putExtra("MEAL_YOUTUBE_LINK", youtubeLink!!)
                startActivity(intent)
            }
        }
    }

    private fun getMealId() {
        mealId = intent.getStringExtra("MEAL_ID").toString()
        Log.d("MEAL_DB", "From getMealId()" + mealId!!)
        getMealDataById()
    }

    private fun getMealDataById() {
        if (isConnected) {
            // Get Data From Api
            viewModel.getMealById(mealId!!).observe(this, Observer { meal ->
                if (meal != null) {
                    this.meal = meal
                    if (!meal.strYoutube.isNullOrEmpty()) {
                        youtubeLink = meal.strYoutube.toString()
                    } else {
                        val str = Regex("[^A-Za-z0-9]").replace(meal.strMeal.toString(), "")
                        if (MealsYoutubeLinks.isInEnum(str)) {
                            youtubeLink = MealsYoutubeLinks.valueOf(str).strYoutube
                        }
                    }
                    binding.btnYoutube.visibility = View.VISIBLE
                    setDataInViews()
                    getIngredientsList()
                }
            })
        } else {
            // Getting meal from Room Database
            Log.d("MEAL_DB", "from getMealDataById() mealId = " + mealId!!)
            viewModel.getMealFromDB(mealId).observe(this, Observer { meal ->
                if(meal != null){
                    this.meal = meal
                    Log.d("MEAL_DB", "from getMealDataById() meal = " + this.meal!!.strMeal)
                    setDataInViews()
                    getIngredientsList()
                }
            })
        }
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
        if (isConnected) {
            if (meal!!.strInstructions?.elementAt(0) == '.') {
                Log.d("REMOVE_DOT", meal!!.strInstructions!!.elementAt(0).toString())
                meal!!.strInstructions = meal!!.strInstructions!!.addCharAtIndex('1', 0)
            }
            if (!meal!!.strInstructions?.contains("\r\n\r\n")!!) {
                meal!!.strInstructions = meal!!.strInstructions?.replace(".\r\n", "\r\n\r\n")
            }
            meal!!.strInstructions = meal!!.strInstructions?.trim()
        }else{
            youtubeLink = meal!!.strYoutube.toString()
            binding.btnYoutube.visibility = View.VISIBLE
        }
        binding.tvInstructions.text = meal!!.strInstructions
        binding.headingInstructions.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        isMealFav()
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
        binding.rvIngredients.removeItemDecoration(customItemMargin)
        ingredientsAdapter.setIngredientsList(ingredientsList)
        binding.rvIngredients.apply {
            addItemDecoration(customItemMargin)
            adapter = ingredientsAdapter
            layoutManager =
                LinearLayoutManager(this@MealActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.headingIngredients.visibility = View.VISIBLE
    }

    private fun isMealFav() {

        viewModel.isMealFavorite(mealId).observe(this, Observer { isFavorite ->
            if (isFavorite) {
                // The meal is a favorite
                binding.mealFavoriteBtn.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                // The meal is not a favorite
                binding.mealFavoriteBtn.setImageResource(R.drawable.ic_favorite)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        networkConnection.unregisterNetworkCallback()
    }
}