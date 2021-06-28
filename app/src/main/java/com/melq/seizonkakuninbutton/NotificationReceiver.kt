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
import androidx.core.content.getSystemService
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.melq.seizonkakuninbutton.model.user.UserRepository
import java.util.*

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val requestCode = intent!!.getIntExtra("RequestCode", 0)

        if (requestCode == 2) {
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(R.string.app_name)

            FirebaseApp.initializeApp(context)
            val user = Firebase.auth.currentUser
            if (user != null) {
                UserRepository().reportLiving(user.uid, Timestamp.now())
            }
            setNotification(context)
            return
        }

        val intentToMainActivity = Intent(context, MainActivity::class.java).apply {
            putExtra("RequestCode", requestCode)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intentToMainActivity,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intentToPushButton = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("RequestCode", 2)
        }
        val pushButtonPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            intentToPushButton,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = context!!.getString(R.string.app_name)
        val title = context.getString(R.string.app_title)
        val message = context.getString(R.string.notification_text)

        val channel = NotificationChannel(
            channelId,
            title,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = message
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val calendar = Calendar.getInstance()
        val now = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_perm_identity_vector)
            .setContentTitle(now)
            .setContentText(message)
            .setColor(context.getColor(R.color.secondary))
            .setContentIntent(pendingIntent)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .addAction(R.drawable.common_google_signin_btn_icon_dark,
                context.getString(R.string.push_button),
                pushButtonPendingIntent)
            .build()

        notificationManager.notify(R.string.app_name, builder)
    }

    private fun setNotification(context: Context?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(R.string.app_name)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.SECOND, 3)

        val requestCode = 1
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(context, NotificationReceiver::class.java).apply { putExtra("RequestCode", 1) },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val am: AlarmManager = context.getSystemService()!!
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}