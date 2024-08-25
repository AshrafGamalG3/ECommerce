package com.example.ecommerce.ui.auth.presentation

import android.os.Bundle
import android.util.Log
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
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentRegisterBinding
import com.example.ecommerce.ui.auth.model.User
import com.example.ecommerce.utils.RegisterValidation
import com.example.ecommerce.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)
        onClick()
        registerObserver()

    }

    private fun onClick() {
        binding.buttonRegister.setOnClickListener {
            validUser()
        }

        binding.tvDoYouHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun validUser() {

        val fName = binding.edFirstNameRegister.text.toString().trim()
        val lName = binding.edLastNameRegister.text.toString().trim()
        val email = binding.edEmailRegister.text.toString().trim()
        val password = binding.edPasswordRegister.text.toString().trim()

        if (fName.isEmpty() || lName.isEmpty()) {
            if (fName.isEmpty()) binding.edFirstNameRegister.error = "First name cannot be empty"
            if (lName.isEmpty()) binding.edLastNameRegister.error = "Last name cannot be empty"
        } else {
            registerAccount(fName, lName, email, password)
        }
    }

    private fun registerAccount(fName: String, lName: String, email: String, password: String) {
        viewModel.createAccountWithEmailAndPass(User(fName, lName, email), password)

    }

    private fun registerObserver() {
        lifecycleScope.launch {

                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.register.collect { resource ->
                        when (resource) {
                            is Resources.Loading -> {
                                binding.buttonRegister.startAnimation()
                            }

                            is Resources.Success -> {
                                Log.e("TAG", "registerObserver: ${resource.data.toString()} ")
                                binding.buttonRegister.revertAnimation()
                                Toast.makeText(requireContext(),"you have created new account, login now", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                            }

                            is Resources.Error -> {
                                Log.e("TAG", "registerObserver: ${resource.message.toString()}")
                                binding.buttonRegister.revertAnimation()
                            }

                            else -> Unit
                        }
                    }
                }

        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validation.collect { validation ->
                    if (validation.email is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.edEmailRegister.apply {
                                requestFocus()
                                error = validation.email.message
                            }
                        }


                    }
                    if (validation.password is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.edPasswordRegister.apply {
                                requestFocus()
                                error = validation.password.message
                            }
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}