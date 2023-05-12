package com.one.easyfood

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.one.easyfood.databinding.ActivityMealBinding
import com.one.easyfood.models.MealsList
import com.one.easyfood.repository.ApiRepository
import com.one.easyfood.retrofit.RetrofitRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataFromOtherView()
        getMealData()

        onBackArrowClick()

    }

    private fun getMealData() {
        RetrofitRequest.apiRequest.getMealsById(mealId).enqueue(object : Callback<MealsList>{
            override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                if(response.body()!=null){
                    binding.tvMealName.text = response.body()!!.meals[0].strMeal
                    Glide.with(this@MealActivity)
                        .load(response.body()!!.meals[0].strMealThumb)
                        .into(binding.imgMeal)
                }
            }

            override fun onFailure(call: Call<MealsList>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getDataFromOtherView() {
        mealId = intent.getStringExtra("MEAL_ID").toString()
    }

    private fun onBackArrowClick() {
        binding.imgBackArrow.setOnClickListener{
            finish()
        }
    }
}