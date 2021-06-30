package com.melq.seizonkakuninbutton.page.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.melq.seizonkakuninbutton.MainViewModel
import com.melq.seizonkakuninbutton.R
import com.melq.seizonkakuninbutton.databinding.FragmentWatcherHistoryBinding

class WatcherHistoryFragment : Fragment(R.layout.fragment_watcher_history) {
    val vm: MainViewModel by activityViewModels()

    var _binding: FragmentWatcherHistoryBinding? = null
    val binding: FragmentWatcherHistoryBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWatcherHistoryBinding.bind(view)
    }
}