package com.melq.notalone.page.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.melq.notalone.MainViewModel
import com.melq.notalone.R
import com.melq.notalone.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        requireActivity().setTitle(R.string.log_in)

        binding.btLogin.setOnClickListener {
            binding.run {
                vm.loginPushed(etEmail.text.toString(), etPassword.text.toString(), cbWatcher.isChecked)
                vm.eMessage.observe(viewLifecycleOwner) {
                    if (it != 0) {
                        Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
                        vm.eMessage.value = 0
                    }
                }
                vm.done.observe(viewLifecycleOwner) {
                    if (it == true) {
                        Snackbar.make(view,
                            "${vm.auth.currentUser!!.email}${getString(R.string.success_login)}",
                            Snackbar.LENGTH_LONG).show()
                        val pref = activity?.getSharedPreferences("preference_root", Context.MODE_PRIVATE)
                        pref?.edit { putBoolean("isWatcher", vm.isWatcher) }
                        findNavController().popBackStack()
                        vm.done.value = false
                    }
                }
            }
        }

        binding.btCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createFragment)
        }
    }
}