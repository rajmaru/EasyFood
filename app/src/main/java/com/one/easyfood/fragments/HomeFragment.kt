package com.one.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.one.easyfood.activities.MealActivity
import com.one.easyfood.adapters.CategoriesChipAdapter
import com.one.easyfood.adapters.PopularMealsAdapter
import com.one.easyfood.adapters.RecommendedAdapter
import com.one.easyfood.databinding.FragmentHomeBinding
import com.one.easyfood.itemdecoration.CustomItemMargin
import com.one.easyfood.models.Category
import com.one.easyfood.models.Meal
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
    private var randomMeal: Meal? = null
    private var categoriesList: List<Category>? = null
    private var popularMeals: List<Meal>? = null
    private var recommendedList: List<Meal>? = null
    private lateinit var factory: DrawableCrossFadeFactory

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
        onClick()
        onRefresh()
    }

    private fun init() {
        networkConnection = NetworkConnection(requireContext())
        viewModel = ViewModelProvider(
            this,
            MealsViewModelFactory(requireContext())
        )[MealsViewModel::class.java]
        categoriesChipAdapter = CategoriesChipAdapter()
        popularMealsAdapter = PopularMealsAdapter()
        recommendedAdapter = RecommendedAdapter()
        customItemMargin = CustomItemMargin()
        factory = DrawableCrossFadeFactory.Builder()
            .setCrossFadeEnabled(true)
            .build()
    }

    private fun checkNetworkConnection() {
        networkConnection.observe(viewLifecycleOwner) { isConnected ->
            this.isConnected = isConnected
            categoriesChipAdapter.setIsConnected(isConnected)
            popularMealsAdapter.setIsConnected(isConnected)
            recommendedAdapter.setIsConnected(isConnected)
            if (isConnected) {
                Log.d("RAJ", "isConnected(): Random Meal")
                callData()
            } else {
                if (randomMeal == null) {
                    Log.d("RAJ", "!isConnected(): Random Meal")
                    binding.tvRandomMeal.visibility = View.GONE
                    binding.imgRandomMeal.visibility = View.GONE
                    binding.imgGradientRandomMeal.visibility = View.GONE
                }
                if (categoriesList == null) {
                    binding.rvHomeCategories.visibility = View.GONE
                    binding.categoriesChipShimmer.visibility = View.VISIBLE
                }
                if (popularMeals == null) {
                    binding.rvHomePopular.visibility = View.GONE
                    binding.popularRvShimmer.visibility = View.VISIBLE
                }
                if (recommendedList == null) {
                    binding.rvHomeRecommended.visibility = View.GONE
                    binding.recommendedRvShimmer.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onRefresh() {
        binding.homeRefresh.setOnRefreshListener {
            if (isConnected && binding.homeRefresh.isRefreshing) {
                binding.homeRefresh.isRefreshing = false
                Log.d("RAJ", "onRefresh()")
                callRandomMeal()
                if (categoriesList == null) {
                    getCategories()
                }
                if (popularMeals == null) {
                    getPopularMeals()
                }
                if (recommendedList == null) {
                    getRecommended()
                }
            } else {
                binding.homeRefresh.isRefreshing = false
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
        if(randomMeal==null){
            callRandomMeal()
        }else{
            binding.tvRandomMeal.text = randomMeal!!.strMeal
            Glide.with(this@HomeFragment)
                .load(randomMeal!!.strMealThumb)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(binding.imgRandomMeal)
            binding.tvRandomMeal.visibility = View.VISIBLE
            binding.imgRandomMeal.visibility = View.VISIBLE
            binding.imgGradientRandomMeal.visibility = View.VISIBLE
        }
    }

    private fun callRandomMeal() {
        viewModel.getRandomMeal().observe(viewLifecycleOwner, Observer { meal ->
            meal?.let {
                randomMeal = meal
                randomMeal?.let {
                    Glide.with(this@HomeFragment)
                        .load(it.strMealThumb)
                        .transition(DrawableTransitionOptions.withCrossFade(factory))
                        .into(binding.imgRandomMeal)
                    binding.tvRandomMeal.text = it.strMeal
                    binding.imgGradientRandomMeal.visibility = View.VISIBLE
                    binding.tvRandomMeal.visibility = View.VISIBLE
                    binding.imgRandomMeal.visibility = View.VISIBLE
                    Log.d("RAJ", "getRandomMeal()")
                }
            }
        })
    }

    private fun getCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner, Observer { categoriesList ->
            categoriesList?.let {
                this.categoriesList = it.categories
                setCategoriesChipRV()
            }
        })
    }

    private fun setCategoriesChipRV() {
        categoriesChipAdapter.setCategoryList(
            requireContext(),
            categoriesList as ArrayList<Category>
        )
        binding.rvHomeCategories.apply {
            removeItemDecoration(customItemMargin)
            addItemDecoration(customItemMargin)
            adapter = categoriesChipAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvHomeCategories.visibility = View.VISIBLE
        binding.categoriesChipShimmer.visibility = View.GONE
    }

    private fun getPopularMeals() {
        viewModel.getPopularMeals("Chicken").observe(viewLifecycleOwner, Observer { popularMeals ->
            popularMeals?.let {
                this.popularMeals = it.meals
                setPopularMealsRV()
            }
        })
    }

    private fun setPopularMealsRV() {
        binding.rvHomePopular.removeItemDecoration(customItemMargin)
        popularMealsAdapter.setPopularMealsList(requireContext(), popularMeals as ArrayList<Meal>)
        binding.rvHomePopular.apply {
            addItemDecoration(customItemMargin)
            adapter = popularMealsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvHomePopular.visibility = View.VISIBLE
        binding.popularRvShimmer.visibility = View.GONE
    }

    private fun getRecommended() {
        viewModel.getRecommendedMeals("Vegetarian")
            .observe(viewLifecycleOwner, Observer { recommendedList ->
                recommendedList?.let {
                    this.recommendedList = it.meals
                    setRecommendedRV()
                }
            })
    }

    private fun setRecommendedRV() {
        binding.rvHomeRecommended.removeItemDecoration(customItemMargin)
        recommendedAdapter.setRecommendedList(requireContext(), recommendedList as ArrayList<Meal>)
        binding.rvHomeRecommended.apply {
            addItemDecoration(customItemMargin)
            adapter = recommendedAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvHomeRecommended.visibility = View.VISIBLE
        binding.recommendedRvShimmer.visibility = View.GONE
    }

    private fun onClick() {
        binding.cardRandomMeal.setOnClickListener {
            if (isConnected) {
                randomMeal?.let {
                    val intent = Intent(requireActivity(), MealActivity::class.java)
                    intent.putExtra("MEAL_ID", it.idMeal)
                    startActivity(intent)
                }
            }
        }
    }
}
