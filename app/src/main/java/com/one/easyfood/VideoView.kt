package com.one.easyfood

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.one.easyfood.databinding.ActivityVideoViewBinding

class VideoView : AppCompatActivity() {
    private lateinit var binding: ActivityVideoViewBinding
    private var url: String? = null
    private lateinit var mediaController: MediaController

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
        val uri = Uri.parse(url)
        mediaController = MediaController(this)
        binding.videoView.setVideoURI(uri)
        binding.videoView.start()

        binding.videoView.setMediaController(mediaController)
        mediaController.setAnchorView(binding.videoView)
    }
}