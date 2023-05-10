package com.one.easyfood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.one.easyfood.models.Meal
import com.one.easyfood.repository.ApiRepository

class ApiViewModel : ViewModel() {

    private var repository: ApiRepository = ApiRepository()
    private lateinit var randomMeal: LiveData<Meal?>

    fun getRandomMeal(): LiveData<Meal?> {
        randomMeal = repository.getRandomMeal()
        return randomMeal
    }

}