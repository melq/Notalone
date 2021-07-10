package com.melq.notalone

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.melq.notalone.databinding.FragmentMainBinding
import com.melq.notalone.model.user.User
import com.melq.notalone.notification.NotificationReceiver

class MainFragment : Fragment(R.layout.fragment_main) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finish()
        }
    }

    override fun onStart() {
        super.onStart()

        val user = vm.auth.currentUser // FEATURE: ここの処理はActivityの初期化処理内に移したい
        if (user != null) {
            Log.d("MAIN_FRAGMENT", "email: ${user.email}, uid: ${user.uid}")
            vm.getUserData()
        } else {
            Log.d("MAIN_FRAGMENT", "no userdata")
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }

        if (vm.canPush.value == false) {
            binding.btMain.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.circle_gray)
            binding.tvBtText.text = vm.countDown.value.toString()
        }

        vm.canPush.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.btMain.background =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ripple_circle)
                binding.tvBtText.text = getString(R.string.push)
            } else {
                binding.btMain.background =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.circle_gray)
            }
        }
        vm.countDown.observe(viewLifecycleOwner) {
            binding.tvBtText.text =
                if (it in 1..10) it.toString()
                else getString(R.string.push)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            binding.toolbar,
            R.string.app_name,
            R.string.app_title
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val addId = 100; val groupId = 200
        var checkList: MutableList<Map<String, String>> = mutableListOf()
        vm.isUserLoaded.observe(viewLifecycleOwner) {
            if (it == true && binding.navigationView.menu.findItem(addId) == null) {
                checkList = vm.user.watchList
                val sub = binding.navigationView.menu.addSubMenu(groupId, Menu.NONE, Menu.NONE, R.string.check_history)
                for (i in checkList.indices) {
                    val item = sub.add(Menu.NONE, i + 1, i, "${checkList[i]["name"]}")
                    item.setIcon(R.drawable.ic_baseline_perm_identity_vector)
                }
                val item = sub.add(Menu.NONE, addId, checkList.size + 1, R.string.add_user)
                item.setIcon(R.drawable.ic_baseline_add_vector)
            }
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_push -> {
                    Log.d("MAIN_FRAGMENT", "push clicked")
                    return@setNavigationItemSelectedListener true
                }
                addId -> {
                    Log.d("MAIN_FRAGMENT", "add clicked")
                    return@setNavigationItemSelectedListener true
                }
                else -> {
                    val index = menuItem.order
                    Log.d("MAIN_FRAGMENT", "${checkList[index]["name"]} clicked")
                    vm.watchUser = User(checkList[index]["id"]!!, "", checkList[index]["name"]!!, mutableListOf(), mutableListOf())
                    findNavController().navigate(R.id.action_mainFragment_to_watcherHistoryFragment)
                }
            }
            return@setNavigationItemSelectedListener true
        }

        binding.btMain.setOnClickListener {
            vm.buttonPushed(binding.etComment.text.toString())
            vm.done.observe(viewLifecycleOwner) {
                if (it == true) {
                    Snackbar.make(view, R.string.button_pushed, Snackbar.LENGTH_SHORT).show()
                    binding.etComment.setText("")
                    vm.done.value = false
                }
            }

            // 通知関連. context含むのでVMに渡せない
            val notificationManagerCompat = NotificationManagerCompat.from(requireContext())
            notificationManagerCompat.cancel(R.string.remind_to_pusher)
            NotificationReceiver.setNotification(context)
        }

        binding.btHistory.setOnClickListener {
            if (vm.isUserLoaded.value == true)
                findNavController().navigate(R.id.action_mainFragment_to_historyFragment)
        }

        binding.btUserInfo.setOnClickListener{
            if (vm.isUserLoaded.value == true) {
                if (vm.auth.currentUser != null) {
                    binding.navigationView.menu.removeGroup(groupId) // ほんとはこれもVMのログアウト時の処理と一緒にしたい
                    findNavController().navigate(R.id.action_mainFragment_to_userInfoFragment)
                } else
                    findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }
    }
}