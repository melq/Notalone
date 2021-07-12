package com.melq.notalone.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.google.firebase.Timestamp
import com.melq.notalone.MainActivity
import com.melq.notalone.R
import java.util.*

class WatcherNotificationReceiver : BroadcastReceiver() {
    companion object {
        fun setNotification(context: Context, timestamp: Timestamp, dangerLine: Int) {
            val calendar = Calendar.getInstance().apply {
                time = timestamp.toDate()
                add(Calendar.HOUR, dangerLine)
            }
            val now = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
            }
            if (now.after(calendar)) return

            val requestCode = 1
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(context, WatcherNotificationReceiver::class.java)
                    .apply { putExtra("RequestCode", requestCode) },
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val am: AlarmManager = context.getSystemService()!!
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (val requestCode = intent!!.getIntExtra("RequestCode", 0)) {
            1 -> {
                val intentToMainActivity =
                    Intent(context, MainActivity::class.java)
                        .apply { putExtra("RequestCode", requestCode) }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    requestCode,
                    intentToMainActivity,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val channelId = context!!.getString(R.string.remind_to_watcher)
                val title = context.getString(R.string.remind_to_watcher)
                val message = context.getString(R.string.lets_watch)

                val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_baseline_perm_identity_vector)
                    .setContentText(message)
                    .setColor(context.getColor(R.color.secondary))
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .build()

                val channel = NotificationChannel(
                    channelId,
                    title,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = message
                }

                val notificationManagerCompat =
                    NotificationManagerCompat.from(context)
                        .apply {
                            createNotificationChannel(channel)
                            notify(R.string.remind_to_watcher, builder)
                        }

                setNotification(context, Timestamp.now(), 24)
            }
        }
    }
}