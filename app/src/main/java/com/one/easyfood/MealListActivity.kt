package com.one.easyfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.easyfood.adapters.MealListAdapter
import com.one.easyfood.databinding.ActivityMealListBinding
import com.one.easyfood.models.Meal
import com.one.easyfood.models.MealsList
import com.one.easyfood.retrofit.RetrofitRequest
import com.one.easyfood.viewmodel.ApiViewModel
import retrofit2.Callback
import retrofit2.Response

class MealListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealListBinding
    private lateinit var viewModel: ApiViewModel
    private lateinit var mealListAdapter: MealListAdapter
    private lateinit var categoryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ApiViewModel::class.java]
        mealListAdapter = MealListAdapter()

        getCategoryName()
        getMealList()
    }

    private fun getCategoryName() {
        categoryName = intent.getStringExtra("CATEGORY_NAME").toString()
    }

    private fun getMealList() {
        RetrofitRequest.apiRequest.getMealsByCategory(categoryName)
            .enqueue(object : Callback<MealsList?> {
                override fun onResponse(
                    call: retrofit2.Call<MealsList?>,
                    response: Response<MealsList?>
                ) {
                    if (response.body() != null) {

                    }
                }

                override fun onFailure(call: retrofit2.Call<MealsList?>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

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