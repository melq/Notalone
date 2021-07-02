package com.melq.seizonkakuninbutton.page.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.melq.seizonkakuninbutton.MainViewModel
import com.melq.seizonkakuninbutton.R
import com.melq.seizonkakuninbutton.databinding.FragmentWatcherHistoryBinding

class WatcherHistoryFragment : Fragment(R.layout.fragment_watcher_history) {
    val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentWatcherHistoryBinding? = null
    private val binding: FragmentWatcherHistoryBinding get() = _binding!!

    override fun onStart() {
        super.onStart()

        if (!vm.isWatcher) findNavController().popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWatcherHistoryBinding.bind(view)

        binding.btUserInfo.setOnClickListener {
            findNavController().navigate(R.id.action_watcherHistoryFragment_to_userInfoFragment)
        }
    }
}