package com.one.easyfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.one.easyfood.adapters.FavoriteMealsAdapter
import com.one.easyfood.databinding.FragmentFavoritesBinding
import com.one.easyfood.models.Meal
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var favMealsAdapter: FavoriteMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            MealsViewModelFactory(this.requireContext())
        )[MealsViewModel::class.java]
        favMealsAdapter = FavoriteMealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFavMeals()
    }

    private fun getFavMeals() {
        setFavMealsRV(viewModel.getFavMeals(viewLifecycleOwner))
    }

    private fun setFavMealsRV(favMeals: List<Meal?>) {
        favMealsAdapter.setFavMealsList(this.requireContext(), favMeals as List<Meal>)
        binding.rvFavorites.apply {
            adapter = favMealsAdapter
            layoutManager = GridLayoutManager(this@FavoritesFragment.requireContext(), 2)
        }
    }

}