package com.one.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        checkNetworkConnection()
        setOnClickListeners()
        setOnRefreshListener()
    }

    private fun init() {
        networkConnection = NetworkConnection(requireContext())
        viewModel = ViewModelProvider(
            this,
            MealsViewModelFactory(requireContext())
        ).get(MealsViewModel::class.java)
        categoriesChipAdapter = CategoriesChipAdapter()
        popularMealsAdapter = PopularMealsAdapter()
        recommendedAdapter = RecommendedAdapter()
        customItemMargin = CustomItemMargin()
    }

    private fun setOnClickListeners() {
        binding.cardRandomMeal.setOnClickListener {
            if (isConnected) {
                val intent = Intent(requireActivity(), MealActivity::class.java)
                intent.putExtra("MEAL_ID", randomMeal.idMeal)
                startActivity(intent)
            } else {
                showToast("No Internet Connection")
            }
        }
    }

    private fun setOnRefreshListener() {
        binding.homeRefresh.setOnRefreshListener {
            if (isConnected && binding.homeRefresh.isRefreshing) {
                binding.homeRefresh.isRefreshing = false
                callData()
            } else {
                binding.homeRefresh.isRefreshing = false
                showToast("No Internet Connection")
            }
        }
    }

    private fun checkNetworkConnection() {
        networkConnection.observe(viewLifecycleOwner) { isConnected ->
            this.isConnected = isConnected
            if (isConnected) {
                callData()
            } else {
                showToast("No Internet Connection")
            }
        }
    }

    private fun callData() {
        getRandomMeal()
        getCategories()
        getPopularMeals()
        getRecommended()
    }

    private fun getRandomMeal() {
        val factory = DrawableCrossFadeFactory.Builder()
            .setCrossFadeEnabled(true)
            .build()
        viewModel.getRandomMeal().observe(viewLifecycleOwner, Observer { meal ->
            meal?.let {
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

    private fun getCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner, Observer { categoriesList ->
            categoriesList?.let {
                setCategoriesChipRV(categoriesList)
            }
        })
    }

    private fun setCategoriesChipRV(categoriesList: CategoryList) {
        binding.rvHomeCategories.removeItemDecoration(customItemMargin)
        categoriesChipAdapter.setCategoryList(requireContext(), categoriesList.categories as ArrayList<Category>, isConnected)
        binding.rvHomeCategories.apply {
            addItemDecoration(customItemMargin)
            adapter = categoriesChipAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.categoriesChipShimmer.visibility = View.GONE
    }

    private fun getPopularMeals() {
        viewModel.getPopularMeals("Chicken").observe(viewLifecycleOwner, Observer { popularMeals ->
            popularMeals?.let {
                setPopularMealsRV(popularMeals)
            }
        })
    }

    private fun setPopularMealsRV(popularMeals: MealsList) {
        binding.rvHomePopular.removeItemDecoration(customItemMargin)
        popularMealsAdapter.setPopularMealsList(requireContext(), popularMeals.meals as ArrayList<Meal>)
        binding.rvHomePopular.apply {
            addItemDecoration(customItemMargin)
            adapter = popularMealsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvHomePopular.visibility = View.VISIBLE
        binding.popularRvShimmer.visibility = View.GONE
    }

    private fun getRecommended() {
        viewModel.getRecommendedMeals("Vegetarian").observe(viewLifecycleOwner, Observer { recommendedList ->
            recommendedList?.let {
                setRecommendedRV(recommendedList)
            }
        })
    }

    private fun setRecommendedRV(recommendedList: MealsList) {
        binding.rvHomeRecommended.removeItemDecoration(customItemMargin)
        recommendedAdapter.setRecommendedList(requireContext(), recommendedList.meals as ArrayList<Meal>)
        binding.rvHomeRecommended.apply {
            addItemDecoration(customItemMargin)
            adapter = recommendedAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvHomeRecommended.visibility = View.VISIBLE
        binding.recommendedRvShimmer.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
