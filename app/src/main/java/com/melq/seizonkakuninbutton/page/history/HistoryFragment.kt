package com.melq.seizonkakuninbutton.page.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.melq.seizonkakuninbutton.MainViewModel
import com.melq.seizonkakuninbutton.R
import com.melq.seizonkakuninbutton.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHistoryBinding.bind(view)
    }
}