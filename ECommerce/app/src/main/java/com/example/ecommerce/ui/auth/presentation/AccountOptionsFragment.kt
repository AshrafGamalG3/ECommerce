package com.example.ecommerce.ui.auth.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentAccountOptionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountOptionsFragment : Fragment() {
    private var _binding : FragmentAccountOptionsBinding?=null
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

        return inflater.inflate(R.layout.fragment_account_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountOptionsBinding.bind(view)
        onClick()
    }

    private fun onClick(){
        binding.buttonLoginAccountOptions.setOnClickListener {
            val action = AccountOptionsFragmentDirections.actionAccountOptionsFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        binding.buttonRegisterAccountOptions.setOnClickListener {
            val action = AccountOptionsFragmentDirections.actionAccountOptionsFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}