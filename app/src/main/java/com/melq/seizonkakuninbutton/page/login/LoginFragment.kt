package com.melq.seizonkakuninbutton.page.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.melq.seizonkakuninbutton.MainViewModel
import com.melq.seizonkakuninbutton.R
import com.melq.seizonkakuninbutton.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.btLogin.setOnClickListener {
            binding.run {
                if (etEmail.text.isBlank() || etPassword.text.isBlank()) {
                    Snackbar.make(layout, R.string.enter_info, Snackbar.LENGTH_SHORT).show()
                } else {
                    vm.loginPushed()
                }
            }
        }

        binding.btCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createFragment)
        }
    }
}