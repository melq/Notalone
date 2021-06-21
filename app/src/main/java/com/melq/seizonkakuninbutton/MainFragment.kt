package com.melq.seizonkakuninbutton

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.melq.seizonkakuninbutton.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {
    private val vm: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {  }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        vm.setUserName("test0@example.com", "test0")

        binding.btMain.setOnClickListener {
//            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            vm.buttonPushed()
        }
    }
}