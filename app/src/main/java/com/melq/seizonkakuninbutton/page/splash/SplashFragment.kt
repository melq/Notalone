package com.melq.seizonkakuninbutton.page.splash

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.melq.seizonkakuninbutton.R
import com.melq.seizonkakuninbutton.databinding.FragmentSplashBinding

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private var _binding: FragmentSplashBinding? = null
    private val binding: FragmentSplashBinding get() = _binding!!

    private val handler = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this._binding = FragmentSplashBinding.bind(view)

        val runnable = Runnable {
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }
        handler.postDelayed(
            runnable,
            2000
        )

        /*binding.constraintLayout.setOnClickListener {
            handler.removeCallbacksAndMessages(null)
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }*/
    }
}