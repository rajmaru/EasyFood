package com.one.easyfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.one.easyfood.adapters.MealListAdapter
import com.one.easyfood.databinding.ActivityMealListBinding
import com.one.easyfood.models.Meal
import com.one.easyfood.networkconnection.NetworkConnection
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class MealListActivity : AppCompatActivity() {
    private lateinit var networkConnection: NetworkConnection
    private lateinit var binding: ActivityMealListBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var mealListAdapter: MealListAdapter

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
        mealListAdapter = MealListAdapter()
    }

    override fun onStop() {
        super.onStop()
        networkConnection.unregisterNetworkCallback()
    }

    private fun checkInternetConnection() {
        networkConnection = NetworkConnection(this)
        networkConnection.observe(this) { isConnected ->
            val message = if (isConnected) {
                "MealListActivity: Connected"
            } else {
                "MealListActivity: Not Connected"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
        mealListAdapter.setMeallist(this, meals as ArrayList<Meal> )
        binding.tvMeallistTotal.text = "Total: ${meals.size.toString()}"
        binding.rvMeallist.apply {
            adapter = mealListAdapter
            layoutManager = GridLayoutManager(this@MealListActivity, 2)
        }
    }
}
