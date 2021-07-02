package com.melq.seizonkakuninbutton

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.melq.seizonkakuninbutton.model.user.UserRepository
import java.util.*

class WatcherNotificationReceiver : BroadcastReceiver() {
    companion object {
        private const val dangerLine = 5

        fun setNotification(context: Context?, lastPush: Timestamp) {
            val calendar = Calendar.getInstance().apply { time = lastPush.toDate() }
            calendar.add(Calendar.SECOND, dangerLine)

            val requestCode = 1
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(context, WatcherNotificationReceiver::class.java).apply { putExtra("RequestCode", requestCode) },
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val am: AlarmManager = context!!.getSystemService()!!
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val channelId = context!!.getString(R.string.app_name)
        val requestCode = intent!!.getIntExtra("RequestCode", 0)
        Log.d("watcher", "called")

        if (requestCode == 1) {
            val pref = context.getSharedPreferences("preference_root", Context.MODE_PRIVATE)
            val mode = pref?.getBoolean("isWatcher", false)
            if (mode == false) return

            FirebaseApp.initializeApp(context)
            val user = Firebase.auth.currentUser
            if (user != null) {
//                UserRepository().getUserData(user.uid) {
//                    val lastPush = it.pushHistory.last()
//                    val calendar = Calendar.getInstance().apply { time = lastPush.toDate() }
//                    val now = Calendar.getInstance().apply { time = Date() }
//
//                    val diffTime = now.timeInMillis - calendar.timeInMillis
//                    val millisOfHours = 1000 * 60 * 60
//                    val diffHours = diffTime / millisOfHours
//                    Log.d("DangerNoti", "diff: $diffHours")
//
//                    if (diffHours >= dangerLine) {
//                        val intentToMainActivity = Intent(context, MainActivity::class.java).apply {
//                            putExtra("RequestCode", 1)
//                        }
//                        val pendingIntent = PendingIntent.getActivity(
//                            context,
//                            1,
//                            intentToMainActivity,
//                            PendingIntent.FLAG_UPDATE_CURRENT
//                        )
//
//                        val title = context.getString(R.string.danger_notification)
//                        val message = context.getString(R.string.danger_message)
//
//                        val builder = NotificationCompat.Builder(context, channelId)
//                            .setSmallIcon(R.drawable.ic_baseline_warning__vector)
//                            .setContentText(message)
//                            .setColor(context.getColor(R.color.secondary))
//                            .setContentIntent(pendingIntent)
//                            .setWhen(System.currentTimeMillis())
//                            .setAutoCancel(true)
//                            .build()
//
//                        val channel = NotificationChannel(
//                            channelId,
//                            title,
//                            NotificationManager.IMPORTANCE_HIGH
//                        ).apply {
//                            description = message
//                        }
//                        val notificationManagerCompat = NotificationManagerCompat.from(context).apply {
//                            createNotificationChannel(channel)
//                            notify(R.string.app_name, builder)
//                        }
//                    } else {
//                        setNotification(context, lastPush)
//                    }
//                }
            }
        }
    }
}