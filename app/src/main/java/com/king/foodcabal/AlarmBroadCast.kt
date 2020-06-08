package com.king.foodcabal


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.king.foodcabal.Notification.NotificationClass


class AlarmBroadCast : BroadcastReceiver() {

    val notification : NotificationClass = NotificationClass()


    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            notification.NotificationRecipe(context)
            notification.createNotificationChannel(context)
        }

        notification.NotificationRecipe(context)
        notification.createNotificationChannel(context)

    }

}
