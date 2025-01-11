/*
 * Copyright (C) 2020-2025 Raf
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package gr.ihu.iee.aboard.android.util.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import gr.ihu.iee.aboard.android.R
import gr.ihu.iee.aboard.android.util.manager.PreferencesManager
import kotlin.random.Random

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val preferencesManager = PreferencesManager(applicationContext)

        if (preferencesManager.notifications) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= 26) {
                val notificationChannel = NotificationChannel(
                    getString(R.string.default_notification_channel_id),
                    getString(R.string.announcements),
                    NotificationManager.IMPORTANCE_HIGH
                )

                notificationChannel.description = getString(R.string.app_name)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Notification.DEFAULT_LIGHTS
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern = longArrayOf(0, 100, 100, 100, 100, 100)
                notificationManager.createNotificationChannel(notificationChannel)
            }

            val id: String = remoteMessage.data["id"] ?: "-"
            val title: String = remoteMessage.data["title"] ?: "-"
            val tag: String = remoteMessage.data["tag"] ?: "-"
            val author: String = remoteMessage.data["author"] ?: "-"

            val notification: NotificationCompat.Builder = NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle(getString(R.string.notification_push, tag))
                .setContentText("$author: $title")
                .setSmallIcon(R.drawable.ic_baseline_announcement_24)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)

            val args = Bundle()
            args.putString("id", id)
            args.putString("title", tag)

            val pendingIntent: PendingIntent = NavDeepLinkBuilder(applicationContext)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.detailsFragment)
                .setArguments(args)
                .createPendingIntent()

            notification.setContentIntent(pendingIntent)

            notificationManager.notify(Random.nextInt(), notification.build())
        }
    }
}