package com.chatmonkey.crosshairapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.View

class FloatingCrosshairView(context: Context) : View(context) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    var crosshairShape: CrosshairView.Shape = CrosshairView.Shape.PENIS
        set(value) {
            field = value
            invalidate() // Redraw the view when the shape changes
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val size = 100f // Base size for crosshair

        when (crosshairShape) {
            CrosshairView.Shape.CIRCLE -> {
                canvas.drawCircle(centerX, centerY, size / 2, paint)
            }
            CrosshairView.Shape.SQUARE -> {
                val left = centerX - size / 2
                val top = centerY - size / 2
                val right = centerX + size / 2
                val bottom = centerY + size / 2
                canvas.drawRect(left, top, right, bottom, paint)
            }
            CrosshairView.Shape.CROSS -> {
                canvas.drawLine(centerX - size / 2, centerY, centerX + size / 2, centerY, paint)
                canvas.drawLine(centerX, centerY - size / 2, centerX, centerY + size / 2, paint)
            }
            CrosshairView.Shape.PENIS -> {
                val penisPath = Path()
                val shaftWidth = size * 0.2f
                val shaftHeight = size * 0.7f
                val headRadius = shaftWidth * 1.5f

                // Shaft
                penisPath.addRect(
                    centerX - shaftWidth / 2,
                    centerY - shaftHeight / 2,
                    centerX + shaftWidth / 2,
                    centerY + shaftHeight / 2,
                    Path.Direction.CW
                )

                // Head
                penisPath.addCircle(
                    centerX,
                    centerY - shaftHeight / 2 - headRadius / 2,
                    headRadius,
                    Path.Direction.CW
                )

                // Testicles (two circles)
                val testicleRadius = shaftWidth * 0.8f
                penisPath.addCircle(
                    centerX - shaftWidth,
                    centerY + shaftHeight / 2 + testicleRadius / 2,
                    testicleRadius,
                    Path.Direction.CW
                )
                penisPath.addCircle(
                    centerX + shaftWidth,
                    centerY + shaftHeight / 2 + testicleRadius / 2,
                    testicleRadius,
                    Path.Direction.CW
                )

                canvas.drawPath(penisPath, paint)
            }
        }
    }
}
