package com.one.easyfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.one.easyfood.adapters.SearchedMealAdapter
import com.one.easyfood.databinding.ActivitySearchBinding
import com.one.easyfood.models.Meal
import com.one.easyfood.networkconnection.NetworkConnection
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var networkConnection: NetworkConnection
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var searchedMealAdapter: SearchedMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchMealTv.requestFocus()

        checkInternetConnection()
        init()
        getSearchedMeal()
    }

    override fun onStop() {
        super.onStop()
        networkConnection.unregisterNetworkCallback()
    }

    private fun checkInternetConnection() {
        networkConnection = NetworkConnection(this)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                Toast.makeText(this, "SearchActivity: Connected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "SearchActivity: Not Connected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init(){
        viewModel = ViewModelProvider(this, MealsViewModelFactory(this))[MealsViewModel::class.java]
        searchedMealAdapter = SearchedMealAdapter()    }

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