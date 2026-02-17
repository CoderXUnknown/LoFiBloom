package com.lofibloom.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class MainActivity : ComponentActivity() {

    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        player = ExoPlayer.Builder(this).build()

        setContent {
            LoFiBloomApp(player)
        }
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }
}

@Composable
fun LoFiBloomApp(player: ExoPlayer) {

    val infinite = rememberInfiniteTransition(label = "gradient")

    val color1 by infinite.animateColor(
        initialValue = Color(0xFF1A1B3A),
        targetValue = Color(0xFF3A1C71),
        animationSpec = infiniteRepeatable(
            animation = tween(8000),
            repeatMode = RepeatMode.Reverse
        ), label = "c1"
    )

    val color2 by infinite.animateColor(
        initialValue = Color(0xFFB388FF),
        targetValue = Color(0xFFFF80AB),
        animationSpec = infiniteRepeatable(
            animation = tween(8000),
            repeatMode = RepeatMode.Reverse
        ), label = "c2"
    )

    var current by remember { mutableStateOf<Station?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(color1, color2)
                )
            )
            .padding(16.dp)
    ) {

        Column {

            Text(
                text = "LoFiBloom 🌸",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(stations) { station ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                current = station

                                player.stop()
                                player.clearMediaItems()

                                val mediaItem = MediaItem.fromUri(station.url)
                                player.setMediaItem(mediaItem)
                                player.prepare()
                                player.play()
                            }
                    ) {
                        Text(
                            text = station.name,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            current?.let {
                Text(
                    text = "Now Playing: ${it.name}",
                    color = Color.White
                )
            }
        }
    }
}
