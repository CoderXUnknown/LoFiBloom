package com.lofibloom.app

data class Station(
    val name: String,
    val url: String
)

val stations = listOf(
    Station(
        "Lofi Girl",
        "https://stream.zeno.fm/0r0xa792kwzuv"
    ),
    Station(
        "Chillhop",
        "https://stream.zeno.fm/f3wvbbqmdg8uv"
    ),
    Station(
        "Lofi Cafe",
        "https://icecast.radiofrance.fr/fip-midfi.mp3"
    )
)
