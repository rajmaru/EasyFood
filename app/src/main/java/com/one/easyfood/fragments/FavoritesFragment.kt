package com.one.easyfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.one.easyfood.adapters.FavoritesListAdapter
import com.one.easyfood.adapters.MealsListAdapter
import com.one.easyfood.databinding.FragmentFavoritesBinding
import com.one.easyfood.itemdecoration.MealListItemMargin
import com.one.easyfood.models.Meal
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var favoritesListAdapter: FavoritesListAdapter
    private lateinit var mealListItemMargin: MealListItemMargin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setOnRefreshListener()
        getFavMeals()
    }

    private fun init() {
        viewModel = ViewModelProvider(
            this,
            MealsViewModelFactory(requireContext())
        )[MealsViewModel::class.java]
        favoritesListAdapter = FavoritesListAdapter()
        mealListItemMargin = MealListItemMargin()
    }

    private fun setOnRefreshListener() {
        binding.favoritesRefresh.setOnRefreshListener {
            if (binding.favoritesRefresh.isRefreshing) {
                binding.favoritesRefresh.isRefreshing = false
                getFavMeals()
            } else {
                binding.favoritesRefresh.isRefreshing = false
            }
        }
    }

    private fun getFavMeals() {
        viewModel.getFavMeals().observe(viewLifecycleOwner, Observer { favMeals ->
            if (favMeals.isNullOrEmpty()) {
                setFavMealsRV(favMeals)
                showEmptyFavoritesLayout()
            } else {
                hideEmptyFavoritesLayout()
                setFavMealsRV(favMeals)
            }
        })
    }

    private fun showEmptyFavoritesLayout() {
        binding.emptyFavoritesLayout.visibility = View.VISIBLE
    }

    private fun hideEmptyFavoritesLayout() {
        binding.emptyFavoritesLayout.visibility = View.GONE
    }

    private fun setFavMealsRV(favMeals: List<Meal>) {
        favoritesListAdapter.setMeallist(requireContext(), favMeals as ArrayList<Meal>)
        binding.rvFavorites.apply {
            removeItemDecoration(mealListItemMargin)
            addItemDecoration(mealListItemMargin)
            adapter = favoritesListAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }
}
