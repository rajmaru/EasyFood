package com.one.easyfood.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealsTypeConverters {

    @TypeConverter
    fun fromAnyToString(attributes: Any?): String{
        if(attributes == null){
            return ""
        }
        return attributes as String
    }

    @TypeConverter
    fun fromStringToAny(attributes: String?): Any{
        if(attributes == null){
            return ""
        }
        return attributes
    }
}