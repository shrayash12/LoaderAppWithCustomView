package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.udacity.Constants.DESCRIPTION
import com.udacity.Constants.SUCCESS
import com.udacity.Constants.TITLE

const val requestCodeNotification = 0
lateinit var notificationManager: NotificationManager
lateinit var notificationChannel: NotificationChannel
const val NOTIFICATION_ID = 0

object NotificationHelper {

    private fun createNotificationBuilder(
            context: Context,
            contentPendingIntent: PendingIntent,
            channelId: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(
                context,
                channelId
        )
                .setContentTitle(
                        context
                                .getString(R.string.notification_title)
                )
                .setContentText(
                        context
                                .getString(R.string.notification_description)
                )
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .addAction(
                        R.drawable.ic_assistant_black_24dp,
                        context.getString(R.string.notification_button),
                        contentPendingIntent
                )
                .setSmallIcon(R.drawable.ic_assistant_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    fun createNotificationChannel(
            context: Context,
            title: String,
            isSuccess: Boolean,
            description: String
    ) {
        // Create the content intent for the notification, which launches
        // this activity
        val detailsIntent = Intent(context, DetailActivity::class.java)
        detailsIntent.putExtra(TITLE, title)
        detailsIntent.putExtra(SUCCESS, isSuccess)
        detailsIntent.putExtra(DESCRIPTION, description)

        // 1
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"

        // FLAG_UPDATE_CURRENT specifies that if a previous
        // PendingIntent already exists, then the current one
        // will update it with the latest intent
        val contentPendingIntent = PendingIntent.getActivity(
                context,
                requestCodeNotification,
                detailsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )


        // checking if android version is greater than oreo(API 26) or not
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationChannel = NotificationChannel(
                    channelId,
                    context
                            .getString(R.string.notification_description),
                    NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            val builder = createNotificationBuilder(context, contentPendingIntent, channelId)

            // Register the channel with the system
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }

    }
}