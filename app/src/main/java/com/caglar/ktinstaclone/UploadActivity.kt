package com.caglar.ktinstaclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.caglar.ktinstaclone.databinding.ActivityMainBinding
import com.caglar.ktinstaclone.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun upload(view: View) {

    }

    fun selectImage(view: View) {

    }
}