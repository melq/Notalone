package com.melq.seizonkakuninbutton.page.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.melq.seizonkakuninbutton.MainViewModel
import com.melq.seizonkakuninbutton.R
import com.melq.seizonkakuninbutton.databinding.FragmentCreateBinding

class CreateFragment: Fragment(R.layout.fragment_create) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentCreateBinding? = null
    private val binding: FragmentCreateBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateBinding.bind(view)
    }
}