package com.one.easyfood.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.one.easyfood.models.CategoryList
import com.one.easyfood.models.Meal
import com.one.easyfood.models.MealsList
import com.one.easyfood.repository.MealsRepository

class MealsViewModel(context: Context) : ViewModel() {

    private var repository: MealsRepository = MealsRepository(context)
    private lateinit var randomMeal: LiveData<Meal?>
    private lateinit var categories: LiveData<CategoryList?>
    private lateinit var popularMeals: LiveData<MealsList?>
    private lateinit var recommendedMeals: LiveData<MealsList?>
    private lateinit var mealsByCategory: LiveData<MealsList?>
    private lateinit var searchedMeals: LiveData<MealsList?>
    private lateinit var mealById: LiveData<Meal?>


    fun getRandomMeal(): LiveData<Meal?> {
        randomMeal = repository.getRandomMeal()
        return randomMeal
    }

    fun getCategories(): LiveData<CategoryList?> {
        categories = repository.getCategories()
        return categories
    }

    fun getPopularMeals(categoryName: String): LiveData<MealsList?> {
        popularMeals = repository.getPopularMeals(categoryName)
        return popularMeals
    }

    fun getRecommendedMeals(categoryName: String): LiveData<MealsList?> {
        recommendedMeals = repository.getRecommendedMeals(categoryName)
        return recommendedMeals
    }

    fun getMealById(mealId: String): LiveData<Meal?> {
        mealById = repository.getMealsById(mealId)
        return mealById
    }

    fun getMealsByCategory(categoryName: String): LiveData<MealsList?> {
        mealsByCategory = repository.getMealsByCategory(categoryName)
        return mealsByCategory
    }

    fun searchMeals(mealName: String): LiveData<MealsList?> {
        searchedMeals = repository.searchMeals(mealName)
        return searchedMeals
    }

    fun saveMeal(meal: Meal) {
        repository.saveMeal(meal)
    }

    fun deleteMeal(meal: Meal) {
        repository.deleteMeal(meal)
    }


}