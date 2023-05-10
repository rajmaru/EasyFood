package com.one.easyfood.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.one.easyfood.models.Meal
import com.one.easyfood.models.MealsList
import com.one.easyfood.retrofit.RetrofitRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository {

    private var randomMeal= MutableLiveData<Meal?>()

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

}