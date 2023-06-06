package com.one.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.one.easyfood.MealActivity
import com.one.easyfood.adapters.CategoriesChipAdapter
import com.one.easyfood.adapters.PopularMealsAdapter
import com.one.easyfood.adapters.RecommendedAdapter
import com.one.easyfood.databinding.FragmentHomeBinding
import com.one.easyfood.itemdecoration.CustomItemMargin
import com.one.easyfood.models.Category
import com.one.easyfood.models.CategoryList
import com.one.easyfood.models.Meal
import com.one.easyfood.models.MealsList
import com.one.easyfood.networkconnection.NetworkConnection
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var networkConnection: NetworkConnection
    private var isConnected: Boolean = false

    private lateinit var viewModel: MealsViewModel

    private lateinit var categoriesChipAdapter: CategoriesChipAdapter
    private lateinit var popularMealsAdapter: PopularMealsAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter

    private lateinit var customItemMargin: CustomItemMargin

    private lateinit var randomMeal: Meal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
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
        init()
        checkNetworkConnection()
        onClick()
        onRefresh()
    }


    private fun init() {
        networkConnection = NetworkConnection(this.requireContext())
        viewModel = ViewModelProvider(
            this,
            MealsViewModelFactory(this.requireContext())
        )[MealsViewModel::class.java]
        categoriesChipAdapter = CategoriesChipAdapter()
        popularMealsAdapter = PopularMealsAdapter()
        recommendedAdapter = RecommendedAdapter()
        customItemMargin = CustomItemMargin()
    }

    private fun onClick(){
        binding.cardRandomMeal.setOnClickListener {
            if(isConnected){
                val intent = Intent(this.activity, MealActivity::class.java)
                intent.putExtra("MEAL_ID", randomMeal.idMeal)
                startActivity(intent)
            }else{
                Toast.makeText(this.requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onRefresh() {
        binding.homeRefresh.setOnRefreshListener {
                if(isConnected && binding.homeRefresh.isRefreshing){
                    binding.homeRefresh.isRefreshing = false
                    callData()
                }else{
                    binding.homeRefresh.isRefreshing = false
                    Toast.makeText(this.requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkNetworkConnection() {
        networkConnection.observe(this.requireActivity()) { isConnected ->
            if(isConnected){
                this.isConnected = isConnected
                callData()
            }else{
                this.isConnected = isConnected
                Toast.makeText(this.requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun callData() {
        getRandomMeal()
        getCategories()
        getPopularMeals()
        getRecommended()
    }

    //Random Meal
    private fun getRandomMeal() {
        val factory = DrawableCrossFadeFactory.Builder()
            .setCrossFadeEnabled(true)
            .build()
        viewModel.getRandomMeal().observe(viewLifecycleOwner, Observer { meal ->
            if (meal != null) {
                randomMeal = meal
                Glide.with(this@HomeFragment)
                    .load(randomMeal.strMealThumb)
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(binding.imgRandomMeal)
                binding.tvRandomMeal.text = randomMeal.strMeal
                binding.imgGradientRandomMeal.visibility = View.VISIBLE
            }
        })
    }

    //Categories Chip
    private fun getCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner, Observer { categoriesList ->
            if (categoriesList != null) {
                setCategoriesChipRV(categoriesList)
            }
        })
    }

    private fun setCategoriesChipRV(categoriesList: CategoryList) {
        binding.rvHomeCategories.removeItemDecoration(customItemMargin)
        categoriesChipAdapter.setCategoryList(
            this.requireContext(),
            categoriesList.categories as ArrayList<Category>,
            isConnected
        )
        binding.rvHomeCategories.apply {
            addItemDecoration(customItemMargin)
            adapter = categoriesChipAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.categoriesChipShimmer.visibility = View.GONE
    }


    //Popular Meals
    private fun getPopularMeals() {
        viewModel.getPopularMeals("Chicken").observe(viewLifecycleOwner, Observer { popularMeals ->
            if (popularMeals != null) {
                setPopularMealsRV(popularMeals)
            }
        })
    }

    private fun setPopularMealsRV(popularMeals: MealsList) {
        binding.rvHomePopular.removeItemDecoration(customItemMargin)
        popularMealsAdapter.setPopularMealsList(
            this.requireContext(),
            popularMeals.meals as ArrayList<Meal>
        )
        binding.rvHomePopular.apply {
            addItemDecoration(customItemMargin)
            adapter = popularMealsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvHomePopular.visibility = View.VISIBLE
        binding.popularRvShimmer.visibility = View.GONE

    }

    //Recommended Meals
    private fun getRecommended() {
        viewModel.getRecommendedMeals("Vegetarian")
            .observe(viewLifecycleOwner, Observer { recommendedList ->
                if (recommendedList != null) {
                    setRecommendedRV(recommendedList)
                }
            })
    }

    private fun setRecommendedRV(recommendedList: MealsList) {
        binding.rvHomeRecommended.removeItemDecoration(customItemMargin)
        recommendedAdapter.setRecommendedList(
            this.requireContext(),
            recommendedList.meals as ArrayList<Meal>
        )
        binding.rvHomeRecommended.apply {
            addItemDecoration(customItemMargin)
            adapter = recommendedAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvHomeRecommended.visibility = View.VISIBLE
        binding.recommendedRvShimmer.visibility = View.GONE
    }
}