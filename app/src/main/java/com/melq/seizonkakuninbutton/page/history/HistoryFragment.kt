package com.melq.seizonkakuninbutton.page.history

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.melq.seizonkakuninbutton.MainViewModel
import com.melq.seizonkakuninbutton.R
import com.melq.seizonkakuninbutton.databinding.FragmentHistoryBinding
import java.util.*

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding get() = _binding!!

    private lateinit var adapter: MyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHistoryBinding.bind(view)

        binding.tvTitle.text = "${vm.user.name} の履歴"

        val historyList = vm.user.pushHistory
        adapter = MyAdapter(historyList, requireContext())
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.divider)!!)
        }
        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.addItemDecoration(dividerItemDecoration)
        }
    }
}