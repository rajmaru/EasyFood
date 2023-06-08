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
import com.one.easyfood.models.Category
import com.one.easyfood.networkconnection.NetworkConnection
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var networkConnection: NetworkConnection
    private var isConnected: Boolean = false

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
    }

    private fun checkNetworkConnection() {
        networkConnection.observe(viewLifecycleOwner) { isConnected ->
            this.isConnected = isConnected
            if (isConnected) {
                 getCategories()
            }
        }
    }

    private fun onRefresh() {
        binding.catgeoriesRefresh.setOnRefreshListener {
            if(isConnected){
                getCategories()
            }else{
                binding.catgeoriesRefresh.isRefreshing = false
            }

        }
    }

    private fun getCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner, Observer { categories ->
            categories?.let {
                categoriesAdapter.setCategoriesList(requireContext(), it.categories as ArrayList<Category>)
            }
            binding.catgeoriesRefresh.isRefreshing = false
            setCategoriesRV()
        })
    }

    private fun setCategoriesRV() {
        binding.rvCategories.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
    }
}
