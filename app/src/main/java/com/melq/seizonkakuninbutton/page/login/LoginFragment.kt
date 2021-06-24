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
                vm.loginPushed(etEmail.text.toString(), etPassword.text.toString())
                vm.eMessage.observe(viewLifecycleOwner) {
                    if (it != 0) {
                        Snackbar.make(binding.layout, it, Snackbar.LENGTH_SHORT).show()
                        vm.eMessage.value = 0
                    }
                }
            }
        }

        binding.btCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createFragment)
        }
    }
}