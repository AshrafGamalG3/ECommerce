package com.example.ecommerce.ui.auth.presentation

import android.content.Intent
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
import com.example.ecommerce.R
import com.example.ecommerce.ShoppingActivity
import com.example.ecommerce.databinding.FragmentLoginBinding
import com.example.ecommerce.utils.Resources
import com.example.ecommerce.ui.dialog.setupBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        onClick()
        loginObserver()

    }

    private fun onClick() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.edEmailLogin.text.toString().trim()
            val password = binding.edPasswordLogin.text.toString().trim()
            loginAccount(email, password)

        }

        binding.tvForgotPasswordLogin.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModel.resetPassword(email)
            }

        }
        binding.googleLogin.setOnClickListener {

        }
        binding.facebookLogin.setOnClickListener {

        }
        binding.tvDontHaveAccount.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }

    private fun loginAccount(email: String, password: String) {
        viewModel.login(email, password)
    }

    private fun loginObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.login.collect { value ->
                    when (value) {
                        is Resources.Loading -> {
                            binding.buttonLogin.startAnimation()
                        }

                        is Resources.Success -> {
                            binding.buttonLogin.revertAnimation()
                            val intent = Intent(requireContext(), ShoppingActivity::class.java)
                            startActivity(intent)
                            activity?.finish()

                        }

                        is Resources.Error -> {
                            binding.buttonLogin.revertAnimation()
                            Toast.makeText(requireContext(), value.message, Toast.LENGTH_SHORT)
                                .show()


                        }

                        else -> Unit
                    }

                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resetPassword.collect {
                    when (it) {
                        is Resources.Loading -> {

                        }

                        is Resources.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "Reset link was sent to your email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Resources.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                        else -> Unit
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}