package com.sandip.mymovie

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class PlayerActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var titleText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.playerView)
        titleText = findViewById(R.id.playerTitle)

        val title = intent.getStringExtra("title") ?: "Video"
        val videoUrl = intent.getStringExtra("video_url") ?: ""

        titleText.text = title

        if (videoUrl.isBlank()) {
            titleText.text = "$title\nVideo link उपलब्ध नहीं है"
            return
        }

        initializePlayer(videoUrl)
    }

    private fun initializePlayer(videoUrl: String) {

        player = ExoPlayer.Builder(this).build()

        playerView.player = player

        val mediaItem = MediaItem.fromUri(videoUrl)

        player?.setMediaItem(mediaItem)

        player?.prepare()

        player?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()

        player?.release()
        player = null
    }
}
