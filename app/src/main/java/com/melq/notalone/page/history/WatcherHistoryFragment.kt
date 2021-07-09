package com.melq.notalone.page.history

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.melq.notalone.MainViewModel
import com.melq.notalone.R
import com.melq.notalone.databinding.FragmentWatcherHistoryBinding
import com.melq.notalone.model.user.User
import com.melq.notalone.notification.WatcherNotificationReceiver

class WatcherHistoryFragment : Fragment(R.layout.fragment_watcher_history) {
    private val vm: MainViewModel by activityViewModels()

    private var _binding: FragmentWatcherHistoryBinding? = null
    private val binding: FragmentWatcherHistoryBinding get() = _binding!!

    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finish()
        }
    }

    override fun onStart() {
        super.onStart()

//        if (!vm.isWatcher) findNavController().popBackStack()
        vm.getWatchUserData(vm.watchUser.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWatcherHistoryBinding.bind(view)

        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            binding.toolbar,
            R.string.app_name,
            R.string.app_title
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val checkList = mutableListOf( // 仮データリスト
            mapOf("name" to "jiro", "id" to "5aUA9tnrVTgj2hvIve2GsnKTB2n2"),
            mapOf("name" to "saburo", "id" to "wJghyQbPx5abVwLdQ9GiZ4VSF9z1"),
            mapOf("name" to "shiro", "id" to "jEBri4JlJmftZLzCTZ4rA50eZfB2")
        )
        val sub = binding.navigationView.menu.addSubMenu(Menu.NONE, Menu.NONE, 10, R.string.check_history)
        for (i in checkList.indices) {
            val item = sub.add(Menu.NONE, i + 1, i, "${checkList[i]["name"]}")
            item.setIcon(R.drawable.ic_baseline_perm_identity_vector)
        }
        val addId = 100
        val item = sub.add(Menu.NONE, addId, checkList.size + 1, R.string.add_user)
        item.setIcon(R.drawable.ic_baseline_add_vector)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_push -> {
                    findNavController().navigate(R.id.action_watcherHistoryFragment_to_mainFragment)
                    return@setNavigationItemSelectedListener true
                }
                addId -> {
                    return@setNavigationItemSelectedListener true
                }
                else -> {
                    val index = menuItem.order
                    vm.watchUser = User(checkList[index]["id"]!!, "", "", mutableListOf())
                    findNavController().navigate(R.id.action_watcherHistoryFragment_to_mainFragment)
                    findNavController().navigate(R.id.action_mainFragment_to_watcherHistoryFragment)
                }
            }
            return@setNavigationItemSelectedListener true
        }

        val historyList = vm.watchUser.pushHistory
        adapter = MyAdapter(historyList, requireContext())
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.divider)!!)
        }
        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.addItemDecoration(dividerItemDecoration)
        }

        vm.isWatchUserLoaded.observe(viewLifecycleOwner) {
            if (it == true) {
                requireActivity().title = "${vm.watchUser.name} の履歴"
                historyList.run {
                    clear()
                    addAll(vm.watchUser.pushHistory)

                    val notificationManagerCompat = NotificationManagerCompat.from(requireContext())
                    notificationManagerCompat.cancel(R.string.remind_to_watcher)
                    if (this.isNotEmpty()) {
                        val lastTime = last()["timestamp"] as Timestamp
                        WatcherNotificationReceiver.setNotification(context, lastTime, 24)
                    }
                }
                adapter.notifyDataSetChanged()
                binding.swipeRefreshLayout.isRefreshing = false

                vm.isWatchUserLoaded.value = false
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            vm.getWatchUserData(vm.watchUser.id)
        }

        binding.btUserInfo.setOnClickListener {
            findNavController().navigate(R.id.action_watcherHistoryFragment_to_userInfoFragment)
        }
    }
}