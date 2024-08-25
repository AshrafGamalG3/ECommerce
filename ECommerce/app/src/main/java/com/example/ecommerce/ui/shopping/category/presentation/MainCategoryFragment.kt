package com.example.ecommerce.ui.shopping.category.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentMainCategoryBinding
import com.example.ecommerce.ui.shopping.category.presentation.adapter.BestDealsAdapter
import com.example.ecommerce.ui.shopping.category.presentation.adapter.BestProductsAdapter
import com.example.ecommerce.ui.shopping.category.presentation.adapter.SpecialProductsAdapter
import com.example.ecommerce.utils.Resources
import com.example.ecommerce.utils.showBottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {
    private var _binding: FragmentMainCategoryBinding? = null
    private val binding get() = _binding!!
    private var specialProductsAdapter = SpecialProductsAdapter()
    private val viewModel: MainCategoryViewModel by viewModels()

    private var bestDealsAdapter = BestDealsAdapter()
    private var bestProductsAdapter = BestProductsAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_category, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainCategoryBinding.bind(view)
        onClick()
        setupSpecialProducts()
        categoryObserver()
        setupBestDealsRv()
        setupBestProducts()

        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                viewModel.fetchBestProducts()
            }
        })

    }

    private fun setupSpecialProducts() {
        binding.rvSpecialProducts.adapter = specialProductsAdapter
    }

    private fun categoryObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.specialProducts.collectLatest {
                    when (it) {
                        is Resources.Loading -> {
                            showLoading()
                        }

                        is Resources.Success -> {
                            specialProductsAdapter.differ.submitList(it.data)
                            hideLoading()
                        }

                        is Resources.Error -> {
                            hideLoading()
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                        else -> Unit
                    }
                }

            }
        }



        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bestDealsProducts.collectLatest {
                    when (it) {
                        is Resources.Loading -> {
                            showLoading()
                        }

                        is Resources.Success -> {
                            bestDealsAdapter.differ.submitList(it.data)
                            hideLoading()
                        }

                        is Resources.Error -> {
                            hideLoading()
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                        else -> Unit
                    }
                }
            }
        }


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bestProducts.collectLatest {
                    when (it) {
                        is Resources.Loading -> {
                            binding.bestProductsProgressbar.visibility = View.VISIBLE
                        }

                        is Resources.Success -> {
                            bestProductsAdapter.differ.submitList(it.data)
                            binding.bestProductsProgressbar.visibility = View.GONE


                        }

                        is Resources.Error -> {

                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            binding.bestProductsProgressbar.visibility = View.GONE

                        }

                        else -> Unit
                    }
                }
            }
        }
    }
private fun onClick(){
    specialProductsAdapter.onClick = {
        val b = Bundle().apply { putParcelable("product",it) }
        findNavController().navigate(R.id.action_homeFragment2_to_productDetailsFragment,b)
    }

    bestDealsAdapter.onClick = {
        val b = Bundle().apply { putParcelable("product",it) }
        findNavController().navigate(R.id.action_homeFragment2_to_productDetailsFragment,b)
    }

    bestProductsAdapter.onClick = {
        val b = Bundle().apply { putParcelable("product",it) }
        findNavController().navigate(R.id.action_homeFragment2_to_productDetailsFragment,b)
    }

}

    private fun setupBestProducts() {
        binding.rvBestProducts.adapter = bestProductsAdapter
    }

    private fun setupBestDealsRv() {
        binding.rvBestDealsProducts.adapter = bestDealsAdapter
    }


    private fun showLoading() {
        binding.mainCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.mainCategoryProgressbar.visibility = View.GONE

    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}