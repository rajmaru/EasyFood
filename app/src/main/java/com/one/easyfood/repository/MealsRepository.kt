package com.one.easyfood.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.one.easyfood.db.MealsDatabase
import com.one.easyfood.models.CategoryList
import com.one.easyfood.models.Meal
import com.one.easyfood.models.MealsList
import com.one.easyfood.retrofit.RetrofitRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealsRepository(context: Context) {

    private var randomMeal = MutableLiveData<Meal?>()
    private var categories = MutableLiveData<CategoryList?>()
    private var popularMeals = MutableLiveData<MealsList?>()
    private var recommendedMeals = MutableLiveData<MealsList?>()
    private var mealById = MutableLiveData<Meal?>()
    private var mealsByCategory = MutableLiveData<MealsList?>()
    private var searchedMeals = MutableLiveData<MealsList?>()
    private val mealsDB = MealsDatabase.getInstance(context).mealsDao

    fun getRandomMeal(): LiveData<Meal?> {
        RetrofitRequest.apiRequest.getRandomMeal().enqueue(object : Callback<MealsList> {
            override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                if (response.body() != null) {
                    randomMeal.value = response.body()!!.meals[0]
                }
            }

            override fun onFailure(call: Call<MealsList>, t: Throwable) {
                Log.d("RandomMeal Repo", t.message.toString())
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

    fun getMealsFromApi(mealId: String): LiveData<Meal?> {
        RetrofitRequest.apiRequest.getMealsById(mealId).enqueue(object : Callback<MealsList> {
            override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                if (response.body() != null) {
                    mealById.value = response.body()!!.meals[0]
                }
            }

            override fun onFailure(call: Call<MealsList>, t: Throwable) {
                Log.d("MealById Repo", t.message.toString())
            }

        })
        return mealById
    }

    fun getMealsByCategory(categoryName: String): LiveData<MealsList?> {
        RetrofitRequest.apiRequest.getMealsByCategory(categoryName)
            .enqueue(object : Callback<MealsList> {
                override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                    if (response.body() != null) {
                        mealsByCategory.value = response.body()
                    }
                }

                override fun onFailure(call: Call<MealsList>, t: Throwable) {
                    Log.d("MealsByCategory Repo", t.message.toString())
                }
            })
        return mealsByCategory
    }

    fun searchMeals(mealName: String): LiveData<MealsList?> {
        RetrofitRequest.apiRequest.searchMeals(mealName)
            .enqueue(object : Callback<MealsList> {
                override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                    if (response.body() != null) {
                        searchedMeals.value = response.body()
                    }
                }

                override fun onFailure(call: Call<MealsList>, t: Throwable) {
                    Log.d("SearchMeals Repo", t.message.toString())
                }
            })
        return searchedMeals
    }

    fun getFavMeals(): LiveData<List<Meal>> {
        return mealsDB.getFavMeals()
    }

    fun getMealFromDB(idMeal: String?): LiveData<Meal>{
        return mealsDB.getMealFromDB(idMeal)
    }

    fun saveMeal(meal: Meal){
            mealsDB.upsert(meal)
    }

    fun deleteMeal(meal: Meal){
            mealsDB.delete(meal)
    }

    suspend fun isMealExistInFavoritesList(idMeal: String?): Int {
        return withContext(Dispatchers.IO) {
            mealsDB.isExist(idMeal)
        }
    }

}