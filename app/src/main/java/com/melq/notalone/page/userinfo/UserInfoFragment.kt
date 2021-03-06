package com.melq.notalone.page.userinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.melq.notalone.MainViewModel
import com.melq.notalone.R
import com.melq.notalone.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment(R.layout.fragment_user_info) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentUserInfoBinding? = null
    private val binding: FragmentUserInfoBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserInfoBinding.bind(view)

        binding.etName.setText(vm.user.name)
        binding.etEmail.setText(vm.firebaseUser.email)

        binding.btUpdate.setOnClickListener {
            vm.updateNameClicked(binding.etName.text.toString())
            vm.eMessage.observe(viewLifecycleOwner) {
                if (it != 0) {
                    Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
                    vm.eMessage.value = 0
                }
            }
        }

        binding.btLogout.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(R.string.logout)
                .setMessage(R.string.ask_logout)
                .setPositiveButton(R.string.ok) { _, _ ->
                    vm.logoutClicked()
                    vm.done.observe(viewLifecycleOwner) {
                        if (it == true) {
                            findNavController().popBackStack()
                            vm.done.value = false
                        }
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
        }

        // FEATURE: ???????????????????????????????????????
    }
}