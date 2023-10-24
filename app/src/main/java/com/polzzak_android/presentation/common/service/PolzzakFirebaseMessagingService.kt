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
import com.google.firebase.messaging.RemoteMessage
import com.polzzak_android.R
import com.polzzak_android.common.util.safeLet
import com.polzzak_android.presentation.feature.root.MainActivity
import timber.log.Timber

class PolzzakFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("onNewToken : $token")
    }

    //TODO 알림 데이터 적용
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("onMessageReceived : ${message.toString()}")
        Timber.d("onMessageReceived\n data : ${message.data} notification : ${message.notification?.body} title : ${message.notification?.title} ${message.notification?.icon}")
        safeLet(message.data["title"], message.data["body"]) { title, content ->
            notify("t1", "t2")
        }
    }

    //TODO 알림 분기처리 구현, small icon 앱아이콘 적용, 로그인 정보 저장(access token 등)
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
    }
}