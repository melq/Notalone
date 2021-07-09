package com.melq.notalone.page.splash

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.melq.notalone.MainViewModel
import com.melq.notalone.R
import com.melq.notalone.databinding.FragmentSplashBinding

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentSplashBinding? = null
    private val binding: FragmentSplashBinding get() = _binding!!

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().setTitle(R.string.app_title)
    }

    override fun onStart() {
        super.onStart()

        val runnable = Runnable {
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }
        handler.postDelayed(
            runnable,
            1300
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this._binding = FragmentSplashBinding.bind(view)

        binding.constraintLayout.setOnClickListener {
            handler.removeCallbacksAndMessages(null)
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }
    }
}