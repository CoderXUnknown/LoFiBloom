package com.lofibloom.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlayerService : MediaSessionService() {

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession

    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()

        createNotificationChannel()
        startForeground(1, buildNotification())
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    fun play(url: String) {
        val item = MediaItem.fromUri(url)
        player.setMediaItem(item)
        player.prepare()
        player.play()
    }

    fun stopPlayback() {
        player.stop()
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, "lofibloom_channel")
            .setContentTitle("LoFiBloom")
            .setContentText("Streaming...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "lofibloom_channel",
                "LoFiBloom Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        mediaSession.release()
        player.release()
        super.onDestroy()
    }
}
