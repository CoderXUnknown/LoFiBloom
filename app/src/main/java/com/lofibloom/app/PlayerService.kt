package com.lofibloom.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import android.app.Service
import android.os.IBinder

class PlayerService : Service() {

    companion object {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val EXTRA_URL = "EXTRA_URL"
    }

    private lateinit var player: ExoPlayer

    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this).build()
        createNotificationChannel()
        startForeground(1, buildNotification("LoFiBloom Playing"))
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
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(text: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "lofibloom_channel")
            .setContentTitle("LoFiBloom 🌸")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "lofibloom_channel",
                "LoFiBloom Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
