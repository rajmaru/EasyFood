package com.one.easyfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.easyfood.adapters.SearchedMealAdapter
import com.one.easyfood.databinding.ActivitySearchBinding
import com.one.easyfood.models.Meal
import com.one.easyfood.viewmodel.ApiViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: ApiViewModel
    private lateinit var searchedMealAdapter: SearchedMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchMealTv.requestFocus()

        viewModel = ApiViewModel()
        searchedMealAdapter = SearchedMealAdapter()

        getSearchedMeal()
    }

    private fun getSearchedMeal() {
        binding.searchMealTv.addTextChangedListener {searchedText->
            viewModel.searchMeals(searchedText.toString()).observe(this, Observer {mealsList->
                if (mealsList != null) {
                    if(!mealsList.meals.isNullOrEmpty()){
                        binding.searchMealRv.visibility = View.VISIBLE
                        searchedMealAdapter.setSearchedMealList(this, mealsList.meals as ArrayList<Meal>)
                        setSearchedMealsRv()
                    }else{
                        binding.searchMealRv.visibility = View.INVISIBLE
                    }
                }
            })
        }
    }

    private fun setSearchedMealsRv() {
        binding.searchMealRv.apply {
            adapter = searchedMealAdapter
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
        }
    }
}