package com.one.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.one.easyfood.MealActivity
import com.one.easyfood.adapters.CategoriesChipAdapter
import com.one.easyfood.adapters.PopularMealsAdapter
import com.one.easyfood.adapters.RecommendedAdapter
import com.one.easyfood.databinding.FragmentHomeBinding
import com.one.easyfood.models.Category
import com.one.easyfood.models.Meal
import com.one.easyfood.viewmodel.ApiViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ApiViewModel
    private lateinit var categoriesChipAdapter: CategoriesChipAdapter
    private lateinit var popularMealsAdapter: PopularMealsAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var randomMeal: Meal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[ApiViewModel::class.java]
        categoriesChipAdapter = CategoriesChipAdapter()
        popularMealsAdapter = PopularMealsAdapter()
        recommendedAdapter = RecommendedAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRefresh()
        getRandomMeal()
        getCategories()
        getPopularMeals()
        getRecommended()

        setCategoriesChipRV()
        setPopularMealsRV()
        setRecommendedRV()

        onClick()
    }

    private fun onRefresh() {
        binding.refresh.setOnRefreshListener {
            getRandomMeal()
            getCategories()
            getPopularMeals()
            getRecommended()
        }
    }

    private fun onClick() {
        binding.cardRandomMeal.setOnClickListener {
            val intent = Intent(this.activity, MealActivity::class.java)
            intent.putExtra("MEAL_ID", randomMeal.idMeal)
            startActivity(intent)
        }
    }

    //Random Meal
    private fun getRandomMeal() {
        viewModel.getRandomMeal().observe(viewLifecycleOwner, Observer { meal ->
            if (meal != null) {
                randomMeal = meal
                Glide.with(this@HomeFragment)
                    .load(randomMeal.strMealThumb)
                    .into(binding.imgRandomMeal)
                binding.tvRandomMeal.text = randomMeal.strMeal
                binding.refresh.isRefreshing = false
            }
        })
    }

    //Categories Chip
    private fun getCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner, Observer { categoriesList ->
            if (categoriesList != null) {
                categoriesChipAdapter.setCategoryList(this.requireContext(), categoriesList.categories as ArrayList<Category>)
            }
        })
    }

    private fun setCategoriesChipRV() {
        binding.rvHomeCategories.adapter = categoriesChipAdapter
        binding.rvHomeCategories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }


    //Popular Meals
    private fun getPopularMeals() {
        viewModel.getPopularMeals("Chicken").observe(viewLifecycleOwner, Observer { popularMeals ->
            if (popularMeals != null) {
                popularMealsAdapter.setPopularMealsList(
                    this.requireContext(),
                    popularMeals.meals as ArrayList<Meal>
                )
            }
        })
    }

    private fun setPopularMealsRV() {
        binding.rvHomePopular.adapter = popularMealsAdapter
        binding.rvHomePopular.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    //Recommended Meals
    private fun getRecommended() {
        viewModel.getRecommendedMeals("Vegetarian")
            .observe(viewLifecycleOwner, Observer { recommendedList ->
                if (recommendedList != null) {
                    recommendedAdapter.setRecommendedList(
                        this.requireContext(),
                        recommendedList.meals as ArrayList<Meal>
                    )
                }
            })
    }

    private fun setRecommendedRV() {
        binding.rvHomeRecommended.adapter = recommendedAdapter
        binding.rvHomeRecommended.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}