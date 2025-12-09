package com.chatmonkey.crosshairapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class CrosshairView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    var crosshairShape: Shape = Shape.CIRCLE
        set(value) {
            field = value
            invalidate() // Redraw the view when the shape changes
        }

    enum class Shape {
        CIRCLE, SQUARE, CROSS, PENIS
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val size = 100f // Base size for crosshair

        when (crosshairShape) {
            Shape.CIRCLE -> {
                canvas.drawCircle(centerX, centerY, size / 2, paint)
            }
            Shape.SQUARE -> {
                val left = centerX - size / 2
                val top = centerY - size / 2
                val right = centerX + size / 2
                val bottom = centerY + size / 2
                canvas.drawRect(left, top, right, bottom, paint)
            }
            Shape.CROSS -> {
                canvas.drawLine(centerX - size / 2, centerY, centerX + size / 2, centerY, paint)
                canvas.drawLine(centerX, centerY - size / 2, centerX, centerY + size / 2, paint)
            }
            Shape.PENIS -> {
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
