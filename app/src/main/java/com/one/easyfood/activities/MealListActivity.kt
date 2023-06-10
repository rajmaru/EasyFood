package com.one.easyfood.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.one.easyfood.R
import com.one.easyfood.adapters.MealsListAdapter
import com.one.easyfood.databinding.ActivityMealListBinding
import com.one.easyfood.itemdecoration.MealListItemMargin
import com.one.easyfood.models.Meal
import com.one.easyfood.networkconnection.NetworkConnection
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class MealListActivity : AppCompatActivity() {
    private lateinit var networkConnection: NetworkConnection
    private lateinit var binding: ActivityMealListBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var mealsListAdapter: MealsListAdapter
    private lateinit var mealListItemMargin: MealListItemMargin
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkInternetConnection()
        init()
        getCategoryName()
    }

    private fun init() {
        viewModel = ViewModelProvider(this, MealsViewModelFactory(this))[MealsViewModel::class.java]
        mealsListAdapter = MealsListAdapter()
        mealListItemMargin = MealListItemMargin()
        snackbar = Snackbar.make(binding.meallistSnackbarLayout, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
            .setAnchorView(binding.meallistSnackbarLayout)
            .setBackgroundTint(resources.getColor(R.color.snackbar_bg))
            .setTextColor(resources.getColor(R.color.snackbar_text))
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setAction("Cancel"){
                snackbar.dismiss()
            }
            .setActionTextColor(resources.getColor(R.color.snackbar_text))
    }

    override fun onStop() {
        super.onStop()
        networkConnection.unregisterNetworkCallback()
    }

    private fun checkInternetConnection() {
        networkConnection = NetworkConnection(this)
        networkConnection.observe(this) { isConnected ->
            mealsListAdapter.setIsConnected(isConnected)
            if(isConnected){
                if(snackbar.isShown){
                   snackbar.dismiss()
                }
            }else{
                if(!snackbar.isShown){
                    snackbar.show()
                }
            }
        }
    }

    private fun getCategoryName() {
        val categoryName = intent.getStringExtra("CATEGORY_NAME").toString()
        getMealList(categoryName)
    }

    private fun getMealList(categoryName: String) {
        viewModel.getMealsByCategory(categoryName).observe(this, Observer { mealsList ->
            mealsList?.meals?.let { meals ->
                setMealListRV(meals)
            }
        })
    }

    private fun setMealListRV(meals: List<Meal>) {
        mealsListAdapter.setMeallist(this, meals as ArrayList<Meal> )
        binding.tvMeallistTotal.text = "Total: ${meals.size.toString()}"
        binding.rvMeallist.apply {
            removeItemDecoration(mealListItemMargin)
            addItemDecoration(mealListItemMargin)
            adapter = mealsListAdapter
            layoutManager = GridLayoutManager(this@MealListActivity, 2)
        }
    }
}
