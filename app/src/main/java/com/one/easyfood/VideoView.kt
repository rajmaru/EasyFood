package com.one.easyfood

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.one.easyfood.databinding.ActivityVideoViewBinding

class VideoView : AppCompatActivity() {
    private lateinit var binding: ActivityVideoViewBinding
    private var url: String? = null
    private lateinit var mediaController: MediaController
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getUrl()
        preparePlayer()
    }

    private fun getUrl() {
        //   url = intent.getStringExtra("MEAL_YOUTUBE_LINK").toString()
        url = "https://www.youtube.com/ewdSmjMbsP0"
        Log.d("LINK", url!!)
    }

    private fun preparePlayer() {
        player = ExoPlayer.Builder(this).build()
        binding.exoPlayer.player = player
        binding.exoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        var mediaItem = MediaItem.fromUri(url!!)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }
}