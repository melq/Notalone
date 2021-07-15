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
import com.melq.notalone.MainActivity
import com.melq.notalone.R
import java.util.*

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        private const val warningLine = 12

        fun setNotification(context: Context) { // context含むからViewModelに渡せない、どこに置くのが正解？
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.HOUR, warningLine)

            val requestCode = 1
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(context, NotificationReceiver::class.java).apply { putExtra("RequestCode", requestCode) },
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val am: AlarmManager = context.getSystemService()!!
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        when (val requestCode = intent!!.getIntExtra("RequestCode", 0)) {
            1 -> {
                val intentToMainActivity = Intent(context, MainActivity::class.java).apply {
                    putExtra("RequestCode", requestCode)
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    1,
                    intentToMainActivity,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                // FireStore操作でandroid.database.sqlite.SQLiteDatabaseLockedExceptionが出るので、アクションボタンは一旦廃止
                /*val intentToPushButton = Intent(context, NotificationReceiver::class.java).apply {
                    putExtra("RequestCode", 2)
                }
                val pushButtonPendingIntent = PendingIntent.getBroadcast(
                    context,
                    2,
                    intentToPushButton,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )*/

                val calendar = Calendar.getInstance()
                val now = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

                val channelId = context!!.getString(R.string.remind_to_pusher)
                val title = context.getString(R.string.remind_to_pusher)
                val message = context.getString(R.string.notification_text)

                val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_baseline_perm_identity_vector)
                    .setContentTitle(now)
                    .setContentText(message)
                    .setColor(context.getColor(R.color.secondary))
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
//                    .addAction(R.drawable.ic_baseline_perm_identity_vector, context.getString(R.string.push_button), pushButtonPendingIntent)
                    .build()

                val channel = NotificationChannel(
                    channelId,
                    title,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = message
                }

                val notificationManagerCompat = NotificationManagerCompat.from(context).apply {
                    createNotificationChannel(channel)
                    notify(R.string.remind_to_pusher, builder)
                }
            }
            /*2 -> {
                val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_baseline_perm_identity_vector)
                    .setContentText(context.getString(R.string.button_pushed))
                    .setColor(context.getColor(R.color.secondary))
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setTimeoutAfter(1000)
                    .build()
                val notificationManagerCompat = NotificationManagerCompat.from(context)
                notificationManagerCompat.notify(R.string.app_name, builder)

                GlobalScope.launch(Dispatchers.Main) {
                    val firebaseApp = FirebaseApp.initializeApp(context)
                    val user = Firebase.auth.currentUser
                    if (user != null) {
                        UserRepository().reportLiving(user.uid, Timestamp.now())
                    }
                }

                setNotification(context)
                return
            }*/
        }


    }
}