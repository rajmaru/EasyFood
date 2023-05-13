package com.one.easyfood.retrofit

import com.one.easyfood.models.CategoryList
import com.one.easyfood.models.Meal
import com.one.easyfood.models.MealsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {

    @GET("random.php")
    fun getRandomMeal(): Call<MealsList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String): Call<MealsList>

    @GET("lookup.php")
    fun getMealsById(@Query("i") mealId: String): Call<MealsList>
}