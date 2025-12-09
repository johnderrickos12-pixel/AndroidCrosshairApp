package com.chatmonkey.crosshairapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chatmonkey.crosshairapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // You can set properties on your CrosshairView here if needed
        // binding.crosshairView.crosshairShape = CrosshairView.Shape.PENIS
    }
}
