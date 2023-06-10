package com.one.easyfood.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import com.one.easyfood.R
import com.one.easyfood.adapters.MealsListAdapter
import com.one.easyfood.databinding.ActivitySearchBinding
import com.one.easyfood.itemdecoration.MealListItemMargin
import com.one.easyfood.models.Meal
import com.one.easyfood.networkconnection.NetworkConnection
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var mealsListAdapter: MealsListAdapter
    private lateinit var networkConnection: NetworkConnection
    private var isConnected: Boolean = false
    private lateinit var mealsList: List<Meal>
    private var searchedMealName: String? = null
    private lateinit var snackbar: Snackbar
    private lateinit var mealListItemMargin: MealListItemMargin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchMealTv.requestFocus()

        init()
        checkInternetConnection()
        onClick()
    }

    private fun init() {
        viewModel = ViewModelProvider(this, MealsViewModelFactory(this))[MealsViewModel::class.java]
        mealsListAdapter = MealsListAdapter()
        mealListItemMargin = MealListItemMargin()
        snackbar = Snackbar.make(binding.searchSnackbarLayout, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
            .setAnchorView(binding.searchSnackbarLayout)
            .setBackgroundTint(resources.getColor(R.color.snackbar_bg))
            .setTextColor(resources.getColor(R.color.snackbar_text))
            .setAnimationMode(ANIMATION_MODE_SLIDE)
            .setAction("Cancel"){
                snackbar.dismiss()
            }
            .setActionTextColor(resources.getColor(R.color.snackbar_text))
    }

    private fun checkInternetConnection() {
        networkConnection = NetworkConnection(this)
        networkConnection.observe(this) { isConnected ->
            this.isConnected = isConnected
            mealsListAdapter.setIsConnected(isConnected)
            if (isConnected) {
                if(snackbar.isShown){
                    snackbar.dismiss()
                }
                if (searchedMealName != null) {
                    getSearchedMeal(searchedMealName)
                }
            }else{
                if (!snackbar.isShown){
                    snackbar.show()
                }
            }
        }
    }

    private fun onClick() {
        binding.searchMealBtn.setOnClickListener {
            if (isConnected) {
                getSearchedMeal(searchedMealName)
            } else {
            }
        }

        binding.searchMealTv.addTextChangedListener { searchedText ->
            searchedMealName = searchedText.toString()
            getSearchedMeal(searchedMealName)
        }
    }

    private fun getSearchedMeal(searchedMealName: String?) {
        if (searchedMealName != null) {
            viewModel.searchMeals(searchedMealName).observe(this, Observer { mealsList ->
                if (mealsList != null) {
                    if (!mealsList.meals.isNullOrEmpty()) {
                        binding.searchMealRv.visibility = View.VISIBLE
                        this.mealsList = mealsList.meals
                        setSearchedMealsRv(this.mealsList)
                    } else {
                        binding.searchMealRv.visibility = View.INVISIBLE
                    }
                }
            })
        }
    }

    private fun setSearchedMealsRv(mealsList: List<Meal>) {
        mealsListAdapter.setMeallist(this@SearchActivity, mealsList as ArrayList<Meal>)
        binding.searchMealRv.apply {
            removeItemDecoration(mealListItemMargin)
            addItemDecoration(mealListItemMargin)
            adapter = mealsListAdapter
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
        }
    }

    override fun onStop() {
        super.onStop()
        networkConnection.unregisterNetworkCallback()
    }

}