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
import com.one.easyfood.adapters.CategoriesAdapter
import com.one.easyfood.databinding.FragmentCategoriesBinding
import com.one.easyfood.models.Category
import com.one.easyfood.viewmodel.MealsViewModel
import com.one.easyfood.viewmodel.MealsViewModelFactory

class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var viewModel: MealsViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            MealsViewModelFactory(requireContext())
        ).get(MealsViewModel::class.java)
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnRefreshListener()
        getCategories()
        setCategoriesRV()
    }

    private fun setOnRefreshListener() {
        binding.catgeoriesRefresh.setOnRefreshListener {
            getCategories()
        }
    }

    private fun getCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner, Observer { categories ->
            categories?.let {
                categoriesAdapter.setCategoriesList(requireContext(), it.categories as ArrayList<Category>)
            }
            binding.catgeoriesRefresh.isRefreshing = false
        })
    }

    private fun setCategoriesRV() {
        binding.rvCategories.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
    }
}
