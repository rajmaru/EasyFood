package com.one.easyfood.models

enum class MealYoutubeLink(val strYoutube: String) {
    HomemadeMandazi("https://youtu.be/fHe4YjLv-7A");

    companion object {

        fun isInEnum(someString: String): Boolean {
            return try {
                valueOf(someString)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}