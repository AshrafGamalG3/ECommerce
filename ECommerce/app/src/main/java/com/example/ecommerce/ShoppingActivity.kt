package com.example.ecommerce

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce.databinding.ActivityShopingBinding
import com.example.ecommerce.ui.shopping.cart.presentation.CartViewModel
import com.example.ecommerce.utils.Resources
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityShopingBinding
    private val viewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShopingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

    observer()

    }
    private fun observer(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.cartProducts.collectLatest {
                    when (it) {
                        is Resources.Success -> {
                            val count = it.data?.size ?: 0
                            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                            bottomNavigation.getOrCreateBadge(R.id.cartFragment2).apply {
                                number = count
                                backgroundColor = resources.getColor(R.color.g_blue)
                            }
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

}