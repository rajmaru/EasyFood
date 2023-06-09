// CategoriesFragment.kt
package com.one.easyfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide.init
import com.google.android.material.snackbar.Snackbar
import com.one.easyfood.MainActivity
import com.one.easyfood.adapters.CategoriesAdapter
import com.one.easyfood.databinding.FragmentCategoriesBinding
import com.one.easyfood.itemdecoration.CategoriesListItemMargin
import com.one.easyfood.models.Category
import com.one.easyfood.models.CategoryList
import com.one.easyfood.networkconnection.NetworkConnection
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var networkConnection: NetworkConnection
    private var isConnected: Boolean = false
    private lateinit var categoriesListItemMargin: CategoriesListItemMargin
    private var categoriesList: List<Category>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        checkNetworkConnection()
        onRefresh()
    }

    private fun init(){
        networkConnection = NetworkConnection(requireContext())
        viewModel = ViewModelProvider(
            this,
            MealsViewModelFactory(requireContext())
        )[MealsViewModel::class.java]
        categoriesAdapter = CategoriesAdapter()
        categoriesListItemMargin = CategoriesListItemMargin()
    }

    private fun checkNetworkConnection() {
        networkConnection.observe(viewLifecycleOwner) { isConnected ->
            this.isConnected = isConnected
            categoriesAdapter.setIsConnected(isConnected)
            if (isConnected) {
                getCategories()
            }else{
                if(categoriesList == null){
                    binding.rvCategories.visibility = View.GONE
                    binding.categoriesShimmerLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onRefresh() {
        binding.catgeoriesRefresh.setOnRefreshListener {
            if(isConnected){
                binding.catgeoriesRefresh.isRefreshing = false
                if(categoriesList == null){
                    getCategories()
                }
            }else{
                binding.catgeoriesRefresh.isRefreshing = false
                if(categoriesList == null){
                    binding.rvCategories.visibility = View.GONE
                    binding.categoriesShimmerLayout.visibility = View.VISIBLE
                }
            }

        }
    }

    private fun getCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner, Observer { categoriesList ->
            if(categoriesList != null){
                this.categoriesList = categoriesList.categories
                binding.catgeoriesRefresh.isRefreshing = false
                binding.rvCategories.visibility = View.VISIBLE
                binding.categoriesShimmerLayout.visibility = View.GONE
                setCategoriesRV()
            }
        })
    }

    private fun setCategoriesRV() {
        categoriesAdapter.setCategoriesList(requireContext(), categoriesList as ArrayList<Category>)
        binding.rvCategories.apply {
            removeItemDecoration(categoriesListItemMargin)
            addItemDecoration(categoriesListItemMargin)
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
    }
}
