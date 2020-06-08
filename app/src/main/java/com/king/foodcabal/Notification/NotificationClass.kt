package com.king.foodcabal.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.king.foodcabal.MainActivity
import com.king.foodcabal.R

class NotificationClass {

    var CHANNEL_ID = "com.king.foodcabal.ANDROID"

    fun createNotificationChannel(ctx : Context) {
        var CHANNEL_ID = "com.king.foodcabal.ANDROID"
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ANDROID_CHANNEL"
            val descriptionText = "Simple text here"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun NotificationRecipe(ctx: Context){
        var title = ctx.getString(R.string.notificationTitle)
        var text = ctx.getString(R.string.notificationContent)

        //set a sound when notification is sent
        var alarmRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        var mp : MediaPlayer? = MediaPlayer.create(ctx, alarmRingtone)
//        mp!!.start()

        // Create an explicit intent for an Activity in your app
        val intent = Intent(ctx, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0)

        val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.drawable.applogo)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(text))
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(alarmRingtone)

        with(NotificationManagerCompat.from(ctx)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, builder.build())
        }
    }


}