package com.one.easyfood.retrofit

import com.one.easyfood.models.MealsList
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequest {

    @GET("random.php")
    fun getRandomMeal(): Call<MealsList>

}