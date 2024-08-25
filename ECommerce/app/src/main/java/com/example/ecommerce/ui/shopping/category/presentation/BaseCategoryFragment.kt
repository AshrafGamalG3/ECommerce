package com.example.ecommerce.ui.shopping.category.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentBaseCategoryBinding
import com.example.ecommerce.ui.shopping.category.presentation.adapter.BestDealsAdapter
import com.example.ecommerce.ui.shopping.category.presentation.adapter.BestProductsAdapter
import com.example.ecommerce.utils.showBottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseCategoryFragment : Fragment() {
private var _binding: FragmentBaseCategoryBinding? = null
private val binding get() = _binding!!

    protected var offerAdapter = BestDealsAdapter()
    protected var bestProductsAdapter = BestProductsAdapter()

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
        return inflater.inflate(R.layout.fragment_base_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBaseCategoryBinding.bind(view)
        setupBestProductsRv()
        setupOfferRv()
        onClick()
    }

    private fun onClick(){
        bestProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
//            findNavController().navigate(R.id.action_homeFragment2_to_productDetailsFragment,b)
        }

        offerAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment2_to_productDetailsFragment,b)
        }

        binding.rvOfferProducts.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1) && dx != 0){
                onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY){
                onBestProductsPagingRequest()
            }
        })




    }

    open fun onOfferPagingRequest(){

    }
   open fun onBestProductsPagingRequest(){

    }
    fun showOfferLoading(){
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideOfferLoading(){
        binding.offerProductsProgressBar.visibility = View.GONE
    }

    fun showBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.GONE
    }

    private fun setupBestProductsRv() {
        binding.rvBestProducts.adapter=bestProductsAdapter

    }

    private fun setupOfferRv() {
        binding.rvOfferProducts.adapter=offerAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()

    }

}