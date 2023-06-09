package com.one.easyfood.activities

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.one.easyfood.databinding.ActivityVideoViewBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class VideoView : AppCompatActivity() {
    private lateinit var binding: ActivityVideoViewBinding
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideStatusBar()
        getUrl()
        prepareYoutubePlayer()
    }

    private fun hideStatusBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun getUrl() {
        url = intent.getStringExtra("MEAL_YOUTUBE_LINK").toString()
        url = url.replace("https://www.youtube.com/watch?v=", "")
        Log.d("LINK", url)
    }

    private fun prepareYoutubePlayer() {
        lifecycle.addObserver(binding.youtubePlayerView)

        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(url, 0F)
            }
        })
    }
}
