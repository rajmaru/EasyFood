package com.one.easyfood.db

import android.content.Context
import android.text.style.QuoteSpan
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.one.easyfood.models.Meal

@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(MealsTypeConverters::class)
abstract class MealsDatabase : RoomDatabase() {
    abstract val mealsDao: MealsDao

    companion object {
        @Volatile
        private var INSTANCE: MealsDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MealsDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context,
                        MealsDatabase::class.java,
                        "MealsDB")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as MealsDatabase
        }

    }

}