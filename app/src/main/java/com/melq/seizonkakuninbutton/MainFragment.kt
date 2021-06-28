package com.melq.seizonkakuninbutton

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.melq.seizonkakuninbutton.databinding.FragmentMainBinding
import java.util.*

class MainFragment : Fragment(R.layout.fragment_main) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    lateinit var am: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = vm.auth.currentUser // FEATURE: ここの処理はActivityの初期化処理内に移したい
        if (user != null) {
            vm.getUserData()
            Log.d("MAIN_FRAGMENT", "email: ${user.email}, uid: ${user.uid}")
        } else {
            Log.d("MAIN_FRAGMENT", "no userdata")
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        binding.btMain.setOnClickListener {
            vm.buttonPushed()

            // 通知関連. context含むのでVMに渡せない
            val notificationManagerCompat = NotificationManagerCompat.from(requireContext())
            notificationManagerCompat.cancel(R.string.app_name)
            NotificationReceiver.setNotification(context)
        }

        binding.btHistory.setOnClickListener {
            if (vm.user != null)
                findNavController().navigate(R.id.action_mainFragment_to_historyFragment)
        }

        binding.btUserInfo.setOnClickListener{
            if (vm.user != null) {
                if (vm.auth.currentUser != null) {
                    findNavController().navigate(R.id.action_mainFragment_to_userInfoFragment)
                } else
                    findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }

        // FEATURE: 見る側の画面も追加する
    }
}