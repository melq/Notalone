package com.melq.seizonkakuninbutton.page.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

        val date = Date()
        date.hours = 3
        date.minutes = 32
        val historyList = mutableListOf<Timestamp>(Timestamp(date), Timestamp.now())
        adapter = MyAdapter(historyList)
        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
    }
}