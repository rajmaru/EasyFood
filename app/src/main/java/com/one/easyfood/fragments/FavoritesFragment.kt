package com.one.easyfood.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.one.easyfood.adapters.FavoriteMealsAdapter
import com.one.easyfood.databinding.FragmentFavoritesBinding
import com.one.easyfood.models.Meal
import com.one.easyfood.networkconnection.NetworkConnection
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var favMealsAdapter: FavoriteMealsAdapter
    private lateinit var networkConnection: NetworkConnection
    private var isConnected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
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

        init()
        checkNetworkConnection()
        onRefresh()
    }

    private fun checkNetworkConnection() {
        networkConnection.observe(this.requireActivity()) { isConnected ->
            if(isConnected){
                this.isConnected = isConnected
                getFavMeals()
            }else{
                this.isConnected = isConnected
                Toast.makeText(this.requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init(){
        networkConnection = NetworkConnection(this.requireContext())
        viewModel = ViewModelProvider(
            this,
            MealsViewModelFactory(this.requireContext())
        )[MealsViewModel::class.java]
        favMealsAdapter = FavoriteMealsAdapter()
    }

    private fun onRefresh() {
        binding.favoritesRefresh.setOnRefreshListener {
            if(isConnected && binding.favoritesRefresh.isRefreshing){
                binding.favoritesRefresh.isRefreshing = false
                getFavMeals()
            }else{
                binding.favoritesRefresh.isRefreshing = false
                Toast.makeText(this.requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFavMeals() {
        viewModel.getFavMeals().observe(viewLifecycleOwner, Observer {favMeals->
            if(favMeals.isNullOrEmpty()){
                binding.emptyFavoritesLayout.visibility = View.VISIBLE
            }else{
                binding.emptyFavoritesLayout.visibility = View.GONE
                setFavMealsRV(favMeals)
            }
        })
    }

    private fun setFavMealsRV(favMeals: List<Meal?>) {
        favMealsAdapter.setFavMealsList(this.requireContext(), favMeals as List<Meal>)
        binding.rvFavorites.apply {
            adapter = favMealsAdapter
            layoutManager = GridLayoutManager(this@FavoritesFragment.requireContext(), 2)
        }
    }

}