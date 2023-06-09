package com.one.easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.one.easyfood.models.Meal

@Dao
interface MealsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(meal: Meal)

    @Delete
    fun delete(meal: Meal)

    @Query("SELECT * FROM mealinformation")
    fun getFavMeals(): LiveData<List<Meal>>

    @Query("SELECT idMeal FROM mealinformation WHERE idMeal = :idMeal")
    fun isExist(idMeal : String?) : Int

    @Query("SELECT idMeal FROM mealinformation WHERE idMeal = :idMeal")
    fun getMealFromDB(idMeal : String?) : LiveData<Meal>
}