package com.melq.seizonkakuninbutton

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val requestCode = intent!!.getIntExtra("RequestCode", 0)
        val intentToMainActivity = Intent(context, MainActivity::class.java).apply {
            putExtra("RequestCode", requestCode)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intentToMainActivity,
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

        val builder = Notification.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_perm_identity_vector)
            .setContentTitle(now)
            .setContentText(message)
            .setColor(context.getColor(R.color.secondary))
            .setContentIntent(pendingIntent)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
//            .addAction(R.drawable.common_google_signin_btn_icon_dark,
//                context.getString(R.string.push_button),
//                pushButtonPendingIntent)
            .build()

        notificationManager.notify(R.string.app_name, builder)
    }
}