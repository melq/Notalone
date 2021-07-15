package com.melq.notalone.page.history

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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

        vm.getWatchUsersData(vm.watchUser.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWatcherHistoryBinding.bind(view)

        binding.toolbar.title = "${vm.watchUser.name}の履歴"

        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            binding.toolbar,
            R.string.app_name,
            R.string.app_title
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val checkList = vm.user.watchList
        val sub = binding.navigationView.menu.addSubMenu(Menu.NONE, Menu.NONE, 10, R.string.check_history)
        for (i in checkList.indices) {
            val item = sub.add(Menu.NONE, i + 1, i, "${checkList[i]["name"]}")
            item.setIcon(R.drawable.ic_baseline_perm_identity_vector)
        }
        val addId = 100
        val item = sub.add(Menu.NONE, addId, checkList.size + 1, R.string.add_user)
        item.setIcon(R.drawable.ic_baseline_add_vector)

        val pref = requireContext().getSharedPreferences("preference_root", Context.MODE_PRIVATE)
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_push -> {
                    pref.edit { putInt("lastFragment", -1) }
                    findNavController().navigate(R.id.action_watcherHistoryFragment_to_mainFragment)
                    return@setNavigationItemSelectedListener true
                }
                addId -> {
                    val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_add_watch, null)
                    val etAddEmail: EditText = dialogView.findViewById(R.id.et_add_email)
                    val tvEMessage: TextView = dialogView.findViewById(R.id.tv_e_message)
                    val dialog = AlertDialog.Builder(requireContext())
                        .setTitle(R.string.add_account)
                        .setView(dialogView)
                        .setPositiveButton(R.string.ok, null)
                        .setNegativeButton(R.string.cancel) { _, _ -> }
                        .create()
                    dialog.show()
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        vm.addUserButtonClicked(etAddEmail.text.toString())
                        vm.doneAdd.observe(viewLifecycleOwner) {
                            if (it == true) {
                                dialog.cancel()
                                val index = checkList.size - 1
                                vm.watchUser = User(checkList[index]["id"]!!, "", checkList[index]["name"]!!, mutableListOf(), mutableListOf())
                                pref.edit { putInt("lastFragment", index) }
                                findNavController().navigate(R.id.action_watcherHistoryFragment_to_mainFragment)
                                findNavController().navigate(R.id.action_mainFragment_to_watcherHistoryFragment)

                                Snackbar.make(view, R.string.user_added, Snackbar.LENGTH_SHORT).show()
                                vm.doneAdd.value = false
                            }
                        }
                        vm.eMessage.observe(viewLifecycleOwner) { eMessage ->
                            if (eMessage != 0) {
                                tvEMessage.visibility = View.VISIBLE
                                tvEMessage.setText(eMessage)
                                vm.eMessage.value = 0
                            }
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }
                else -> {
                    val index = menuItem.order
                    vm.watchUser = User(checkList[index]["id"]!!, "", checkList[index]["name"]!!, mutableListOf(), mutableListOf())
                    pref.edit { putInt("lastFragment", index) }
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
                historyList.run {
                    clear()
                    addAll(vm.watchUser.pushHistory)

                    val notificationManagerCompat = NotificationManagerCompat.from(requireContext())
                    notificationManagerCompat.cancel(R.string.remind_to_watcher)
                    if (this.isNotEmpty()) {
                        val lastTime = last()["timestamp"] as Timestamp
                        WatcherNotificationReceiver.setNotification(requireContext(), lastTime, 24)
                    }
                }
                adapter.notifyDataSetChanged()
                binding.swipeRefreshLayout.isRefreshing = false
                vm.isWatchUserLoaded.value = false
            }
        }
        vm.eMessage.observe(viewLifecycleOwner) {
            if (it != 0) {
                Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
                binding.swipeRefreshLayout.isRefreshing = false
                vm.eMessage.value = 0
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            vm.getWatchUsersData(vm.watchUser.id)
        }

        binding.btDeleteUser.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setMessage(R.string.ask_delete_user)
                .setPositiveButton(R.string.ok) { _, _ ->
                    vm.deleteUserButtonClicked()
                    vm.done.observe(viewLifecycleOwner) {
                        if (it == true) {
                            pref.edit { putInt("lastFragment", -1) }
                            findNavController().navigate(R.id.action_watcherHistoryFragment_to_mainFragment)
//                            Snackbar.make(view, R.string.user_deleted, Snackbar.LENGTH_SHORT).show()
                            vm.done.value = false
                        }
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .create()
            dialog.show()
        }
    }
}