package com.example.ecommerce.ui.shopping.category.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentProductDetailsBinding
import com.example.ecommerce.ui.shopping.cart.presentation.DetailsViewModel
import com.example.ecommerce.ui.shopping.category.data.CartProduct
import com.example.ecommerce.ui.shopping.category.data.Product
import com.example.ecommerce.ui.shopping.category.presentation.adapter.ColorsAdapter
import com.example.ecommerce.ui.shopping.category.presentation.adapter.SizesAdapter
import com.example.ecommerce.ui.shopping.category.presentation.adapter.ViewPager2Images
import com.example.ecommerce.utils.Resources
import com.example.ecommerce.utils.hideBottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private val sizesAdapter = SizesAdapter()
    private val colorsAdapter = ColorsAdapter()
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailsBinding.bind(view)
        val product = args.product
        onCLick(product)
        observer()
        setupSizesRv()
        setupColorsRv()
        setupViewpager()
    }

    private fun onCLick(product: Product) {

        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

        sizesAdapter.onItemClick = {
            selectedSize = it
        }

        colorsAdapter.onItemClick = {
            selectedColor = it
        }
        binding.buttonAddToCart.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(product, 1, selectedColor, selectedSize))
        }

        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description

            if (product.colors.isNullOrEmpty())
                tvProductColors.visibility = View.INVISIBLE
            if (product.sizes.isNullOrEmpty())
                tvProductSize.visibility = View.INVISIBLE
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let { colorsAdapter.differ.submitList(it) }
        product.sizes?.let { sizesAdapter.differ.submitList(it) }
    }

    private fun observer(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addToCart.collectLatest {
                    when (it) {
                        is Resources.Loading -> {
                            binding.buttonAddToCart.startAnimation()
                        }

                        is Resources.Success -> {
                            binding.buttonAddToCart.revertAnimation()
                            binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                        }

                        is Resources.Error -> {
                            binding.buttonAddToCart.stopAnimation()
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }


    private fun setupViewpager() {
        binding.apply {
            viewPagerProductImages.adapter = viewPagerAdapter
        }
    }

    private fun setupColorsRv() {
        binding.rvColors.adapter = colorsAdapter
    }

    private fun setupSizesRv() {
        binding.rvSizes.adapter = sizesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}