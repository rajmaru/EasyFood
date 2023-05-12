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
import com.one.easyfood.viewmodel.ApiViewModel

class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var viewModel: ApiViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ApiViewModel::class.java]
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCategories()
        setCategoriesRV()
    }

    private fun getCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner, Observer { categories ->
            if (categories != null) {
                categoriesAdapter.setCategoriesList(categories = categories.categories as ArrayList<Category>)
            }
        })
    }

    private fun setCategoriesRV() {
        binding.rvCategories.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(context,3)
        }
    }
}