package com.example.ecommerce.ui.shopping.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerce.ui.shopping.category.presentation.AccessoryFragment
import com.example.ecommerce.ui.shopping.category.presentation.ChairFragment
import com.example.ecommerce.ui.shopping.category.presentation.CupboardFragment
import com.example.ecommerce.R
import com.example.ecommerce.ui.shopping.category.presentation.TableFragment
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.ui.shopping.category.presentation.FurnitureFragment
import com.example.ecommerce.ui.shopping.category.presentation.MainCategoryFragment
import com.example.ecommerce.ui.shopping.category.presentation.adapter.HomeViewpagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        val categoryFragments= arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )

        binding.viewpagerHome.isUserInputEnabled=false

        val viewPager2Adapter = HomeViewpagerAdapter(categoryFragments,childFragmentManager,lifecycle)
        binding.viewpagerHome.adapter=viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){
            tab,position->

            when(position){
                0->tab.text="Main"
                1->tab.text="Chair"
                2->tab.text="Cupboard"
                3->tab.text="Table"
                4->tab.text="Accessory"
            }
        }.attach()

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}