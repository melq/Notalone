package com.melq.seizonkakuninbutton.page.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.melq.seizonkakuninbutton.MainViewModel
import com.melq.seizonkakuninbutton.R
import com.melq.seizonkakuninbutton.databinding.FragmentCreateBinding

class CreateFragment: Fragment(R.layout.fragment_create) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentCreateBinding? = null
    private val binding: FragmentCreateBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateBinding.bind(view)

        binding.btCreateAccount.setOnClickListener {
            binding.run {
                vm.createPushed(etName.text.toString(), etEmail.text.toString(), etPassword.text.toString())
                vm.eMessage.observe(viewLifecycleOwner) {
                    if (it != 0) {
                        Snackbar.make(binding.layout, it, Snackbar.LENGTH_SHORT).show()
                        vm.eMessage.value = 0
                    }
                }
                vm.done.observe(viewLifecycleOwner) {
                    if (it == true) {Snackbar.make(view,
                        "${vm.auth.currentUser!!.email}${getString(R.string.success_login)}",
                        Snackbar.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_createFragment_to_mainFragment)
                        vm.done.value = false
                    }
                }
            }
        }
    }
}