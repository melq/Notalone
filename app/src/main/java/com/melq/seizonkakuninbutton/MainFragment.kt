package com.melq.seizonkakuninbutton

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.melq.seizonkakuninbutton.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val user = vm.auth.currentUser
        if (user != null) {
            Log.d("MAIN_FRAGMENT", "email: ${user.email}, uid: ${user.uid}")
        } else {
            Log.d("MAIN_FRAGMENT", "no userdata")
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        val intent = Intent(context, MainActivity::class.java)
        NotificationReceiver().onReceive(context, intent)

        binding.btMain.setOnClickListener {
            vm.buttonPushed()
        }

        binding.btUserInfo.setOnClickListener{
            if (vm.auth.currentUser != null) {
                findNavController().navigate(R.id.action_mainFragment_to_userInfoFragment)
            } else
                findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
    }
}