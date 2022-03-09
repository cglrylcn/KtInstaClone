package com.caglar.ktinstaclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.caglar.ktinstaclone.databinding.ActivityFeedBinding
import com.caglar.ktinstaclone.databinding.ActivityUploadBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}