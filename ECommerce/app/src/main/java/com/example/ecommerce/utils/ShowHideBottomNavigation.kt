package com.example.ecommerce.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.example.ecommerce.ShoppingActivity

import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView(){
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(
            com.example.ecommerce.R.id.bottomNavigation
        )
    bottomNavigationView.visibility = android.view.View.GONE
}

fun Fragment.showBottomNavigationView(){
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(
            com.example.ecommerce.R.id.bottomNavigation
        )
    bottomNavigationView.visibility = android.view.View.VISIBLE
}