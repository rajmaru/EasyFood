package com.one.easyfood.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.one.easyfood.models.CategoryList
import com.one.easyfood.models.Meal
import com.one.easyfood.models.MealsList
import com.one.easyfood.retrofit.RetrofitRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository {

    private var randomMeal = MutableLiveData<Meal?>()
    private var categories = MutableLiveData<CategoryList?>()
    private var popularMeals = MutableLiveData<MealsList?>()
    private var recommendedMeals = MutableLiveData<MealsList?>()

    fun getRandomMeal(): LiveData<Meal?> {
        RetrofitRequest.apiRequest.getRandomMeal().enqueue(object : Callback<MealsList> {
            override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                if (response.body() != null) {
                    randomMeal.value = response.body()!!.meals[0]
                }
            }

            override fun onFailure(call: Call<MealsList>, t: Throwable) {
                Log.d("RandomMeal Repo", t.message.toString())
                randomMeal.value = null
            }
        })
        return randomMeal
    }

    fun getCategories(): LiveData<CategoryList?> {
        RetrofitRequest.apiRequest.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null) {
                    categories.value = response.body()
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("Category Repo", t.message.toString())
                categories.value = null
            }
        })
        return categories
    }

    fun getPopularMeals(categoryName: String): LiveData<MealsList?> {
        RetrofitRequest.apiRequest.getMealsByCategory(categoryName)
            .enqueue(object : Callback<MealsList> {
                override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                    if (response.body() != null) {
                        popularMeals.value = response.body()
                    }
                }

                override fun onFailure(call: Call<MealsList>, t: Throwable) {
                    Log.d("PopularMeals Repo", t.message.toString())
                    popularMeals.value = null
                }
            })
        return popularMeals
    }

    fun getRecommendedMeals(categoryName: String): LiveData<MealsList?> {
        RetrofitRequest.apiRequest.getMealsByCategory(categoryName)
            .enqueue(object : Callback<MealsList> {
                override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                    if (response.body() != null) {
                        recommendedMeals.value = response.body()
                    }
                }

                override fun onFailure(call: Call<MealsList>, t: Throwable) {
                    Log.d("Recommended Repo", t.message.toString())
                    recommendedMeals.value = null
                }
            })
        return recommendedMeals
    }

}