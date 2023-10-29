package com.polzzak_android.presentation.common.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.polzzak_android.R
import com.polzzak_android.common.util.safeLet
import com.polzzak_android.presentation.feature.root.MainActivity
import timber.log.Timber

class PolzzakFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("onNewToken : $token")
    }

    override fun handleIntent(intent: Intent?) {
        if (!receiveMessage) return
        intent?.run {
            val keys = this.extras?.keySet()
            keys?.forEach{key->
                Timber.d("$key = ${this.getStringExtra(key)}")
            }
        }

        intent?.extras?.run {
            safeLet(
                getString(
                    NOTIFICATION_TITLE_KEY
                ), getString(NOTIFICATION_BODY_KEY)
            ) { title, body ->
                notify(title, body)
            }
        }
    }

    private fun notify(title: String, content: String) {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            PENDING_INTENT_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notificationBuilder = NotificationCompat.Builder(
            applicationContext,
            NOTIFICATION_CHANNEL_ID
        )
        //TODO 아이콘 적용
        val notification =
            notificationBuilder.setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_background).setContentText(content)
                .setContentIntent(pendingIntent).setAutoCancel(true).build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_NAME = "push alarm"
        private const val NOTIFICATION_CHANNEL_ID = "push_notification_id"
        private const val NOTIFICATION_ID = 1
        private const val PENDING_INTENT_REQUEST_CODE = 1000
        private const val NOTIFICATION_TITLE_KEY = "title"
        private const val NOTIFICATION_BODY_KEY = "body"
        var receiveMessage: Boolean = false
    }
}