package com.lofibloom.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class PlayerService : Service() {

    companion object {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val EXTRA_URL = "EXTRA_URL"
        const val CHANNEL_ID = "lofibloom_channel"
    }

    private lateinit var player: ExoPlayer

    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this).build()

        createNotificationChannel()

        startForeground(
            1,
            buildNotification("LoFiBloom Ready")
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action == ACTION_PLAY) {

            val url = intent.getStringExtra(EXTRA_URL)

            url?.let {

                player.stop()
                player.clearMediaItems()

                player.setMediaItem(MediaItem.fromUri(it))
                player.prepare()
                player.play()

                updateNotification("Playing")
            }
        }

        return START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopPlayerCompletely()
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        stopPlayerCompletely()
        super.onDestroy()
    }

    private fun stopPlayerCompletely() {
        if (::player.isInitialized) {
            player.stop()
            player.release()
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun updateNotification(text: String) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(1, buildNotification(text))
    }

    private fun buildNotification(text: String): Notification {

        val openAppIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("LoFiBloom 🌸")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "LoFiBloom Playback",
                NotificationManager.IMPORTANCE_LOW
            )

            channel.setShowBadge(false)

            val manager =
                getSystemService(NotificationManager::class.java)

            manager.createNotificationChannel(channel)
        }
    }
}
