package com.example.ecommerce.ui.shopping.profile.presentation

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ecommerce.MainActivity
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentProfileBinding
import com.example.ecommerce.utils.Resources
import com.example.ecommerce.utils.showBottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
        private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val viewModel by viewModels<ProfileViewModel>()
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        onClick()
        observer()
    }


    private fun onClick(){
        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment2_to_userAccountFragment)
        }

        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment2_to_ordersFragment)
        }

        binding.linearBilling.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragment2ToBillingFragment(
                0f,
                emptyArray(),
                false
            )
            findNavController().navigate(action)
        }

        binding.linearLogOut.setOnClickListener {
            viewModel.logout()

        }



        binding.linearLogOut.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


    }
    private fun observer() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collectLatest {
                    when (it) {
                        is Resources.Loading -> {
                            binding.progressbarSettings.visibility = View.VISIBLE

                        }
                        is Resources.Success -> {
                            binding.progressbarSettings.visibility = View.GONE
                            Glide.with(requireView()).load(it.data!!.imagePath).error(
                                ColorDrawable(
                                Color.BLACK)
                            ).into(binding.imageUser)
                            binding.tvUserName.text = "${it.data.firstName} ${it.data.lastName}"
                        }
                        is Resources.Error -> {
                            binding.progressbarSettings.visibility = View.GONE
                            binding.tvUserName.text = it.message
                        }
                        else -> Unit

                    }
                }
            }
        }
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