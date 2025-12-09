package com.chatmonkey.crosshairapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager

class FloatingCrosshairService : Service() {

    private var windowManager: WindowManager? = null
    private var floatingCrosshairView: FloatingCrosshairView? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        floatingCrosshairView = FloatingCrosshairView(this)

        val layoutParams = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            )
        } else {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            )
        }

        layoutParams.gravity = Gravity.CENTER
        windowManager?.addView(floatingCrosshairView, layoutParams)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (floatingCrosshairView != null) {
            windowManager?.removeView(floatingCrosshairView)
            floatingCrosshairView = null
        }
    }

    // You can add methods here to change the crosshair shape dynamically
    // For example, from MainActivity via an Intent
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val shapeOrdinal = intent?.getIntExtra("crosshair_shape", -1) ?: -1
        if (shapeOrdinal != -1 && shapeOrdinal < CrosshairView.Shape.values().size) {
            floatingCrosshairView?.crosshairShape = CrosshairView.Shape.values()[shapeOrdinal]
        }
        return START_STICKY
    }
}
