package com.chatmonkey.crosshairapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chatmonkey.crosshairapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val OVERLAY_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // The internal CrosshairView is no longer the primary focus
        // Instead, we'll manage the floating crosshair via a service
        // binding.crosshairView.crosshairShape = CrosshairView.Shape.PENIS

        // Add buttons to control the floating crosshair
        val startFloatingCrosshairButton: Button = binding.root.findViewById(R.id.startFloatingCrosshairButton)
        val stopFloatingCrosshairButton: Button = binding.root.findViewById(R.id.stopFloatingCrosshairButton)

        startFloatingCrosshairButton.setOnClickListener {
            if (checkOverlayPermission()) {
                startFloatingService()
            } else {
                requestOverlayPermission()
            }
        }

        stopFloatingCrosshairButton.setOnClickListener {
            stopFloatingService()
        }
    }

    private fun checkOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName))
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (checkOverlayPermission()) {
                startFloatingService()
                Toast.makeText(this, "Overlay permission granted! Starting service.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Overlay permission denied. Cannot display floating crosshair.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startFloatingService() {
        val serviceIntent = Intent(this, FloatingCrosshairService::class.java)
        // You can pass the desired shape here if you want to dynamically change it
        // serviceIntent.putExtra("crosshair_shape", CrosshairView.Shape.PENIS.ordinal)
        startService(serviceIntent)
        Toast.makeText(this, "Floating crosshair service started.", Toast.LENGTH_SHORT).show()
    }

    private fun stopFloatingService() {
        val serviceIntent = Intent(this, FloatingCrosshairService::class.java)
        stopService(serviceIntent)
        Toast.makeText(this, "Floating crosshair service stopped.", Toast.LENGTH_SHORT).show()
    }
}
