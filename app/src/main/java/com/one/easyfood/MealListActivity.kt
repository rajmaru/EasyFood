package com.one.easyfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.one.easyfood.adapters.MealListAdapter
import com.one.easyfood.databinding.ActivityMealListBinding
import com.one.easyfood.models.Meal
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class MealListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealListBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var mealListAdapter: MealListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MealsViewModelFactory(this))[MealsViewModel::class.java]
        mealListAdapter = MealListAdapter()

        getCategoryName()
    }

    private fun getCategoryName() {
        val categoryName= intent.getStringExtra("CATEGORY_NAME").toString()
        getMealList(categoryName)
    }

    private fun getMealList(categoryName: String) {
        viewModel.getMealsByCategory(categoryName).observe(this, Observer { mealsList ->
            if (mealsList != null) {
                setMealListRV(mealsList.meals as ArrayList<Meal>)
            }
        })
    }


    private fun setMealListRV(meals: ArrayList<Meal>) {
        mealListAdapter.setMeallist(this, meals)
        binding.tvMeallistTotal.text = "Total : ${meals.size.toString()}"
        binding.rvMeallist.apply {
            adapter = mealListAdapter
            layoutManager = GridLayoutManager(this@MealListActivity, 2)
        }
    }

}