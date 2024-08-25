package com.example.ecommerce.ui.shopping.cart.presentation

import android.app.AlertDialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentBillingBinding
import com.example.ecommerce.ui.shopping.cart.data.Address
import com.example.ecommerce.ui.shopping.cart.data.Order
import com.example.ecommerce.ui.shopping.cart.data.OrderStatus
import com.example.ecommerce.ui.shopping.cart.presentation.adapter.AddressAdapter
import com.example.ecommerce.ui.shopping.cart.presentation.adapter.BillingProductsAdapter

import com.example.ecommerce.ui.shopping.category.data.CartProduct
import com.example.ecommerce.ui.shopping.profile.presentation.OrderViewModel
import com.example.ecommerce.utils.HorizontalItemDecoration
import com.example.ecommerce.utils.Resources
import com.google.android.material.snackbar.Snackbar

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment : Fragment() {
    private var _binding: FragmentBillingBinding? = null
    private val binding get() = _binding!!
    private val addressAdapter = AddressAdapter()
    private val billingProductsAdapter = BillingProductsAdapter()
    private val billingViewModel: BillingViewModel by viewModels()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f
    private var selectedAddress: Address? = null
    private val orderViewModel: OrderViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            products = args.product.toList()
            totalPrice = args.totalPrice

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.fragment_billing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBillingBinding.bind(view)

        setupBillingProductsRv()
        setupAddressRv()
        observer()
        onClick()
    }


    private fun onClick() {
        billingProductsAdapter.differ.submitList(products)


        binding.tvTotalPrice.text = "$ $totalPrice"

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        addressAdapter.onClick = {
            selectedAddress = it
        }

        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if (selectedAddress == null) {
                Toast.makeText(requireContext(), "Please select and address", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            showOrderConfirmationDialog()
        }



    }

    private fun showOrderConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order items")
            setMessage("Do you want to order your cart items?")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { dialog, _ ->
                val order = Order(
                    OrderStatus.Ordered.status,
                    totalPrice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }


    private fun observer() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                billingViewModel.address.collectLatest {
                    when (it) {
                        is Resources.Loading -> {
                            binding.progressbarAddress.visibility = View.VISIBLE
                        }

                        is Resources.Success -> {
                            addressAdapter.differ.submitList(it.data)
                            binding.progressbarAddress.visibility = View.GONE
                        }

                        is Resources.Error -> {
                            binding.progressbarAddress.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Error ${it.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        else -> Unit
                    }
                }
            }
        }


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.order.collectLatest {
                    when (it) {
                        is Resources.Loading -> {
                            binding.buttonPlaceOrder.startAnimation()
                        }

                        is Resources.Success -> {
                            binding.buttonPlaceOrder.revertAnimation()
                            findNavController().navigateUp()
                            Snackbar.make(
                                requireView(),
                                "Your order was placed",
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        }

                        is Resources.Error -> {
                            binding.buttonPlaceOrder.revertAnimation()
                            Toast.makeText(
                                requireContext(),
                                "Error ${it.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        else -> Unit
                    }
                }
            }
        }

    }


    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingProductsRv() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = billingProductsAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}