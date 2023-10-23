package com.example.latihan1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View

class CustomLoadingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    val loadingView = findViewById<CustomLoadingView>(R.id.loadingView)
    private val paint = Paint()
    private val rect = RectF()
    private var sweepAngle = 0f
    private val strokeWidth = 20f
    private val startAngle = -90f
    private val maxSweepAngle = 360f
    private val animationDuration = 2000L  // Durasi animasi dalam milidetik

    private var isAnimating = false
    private var currentProgress = 0

    init {
        paint.color = Color.BLUE
        paint.isAntiAlias = true
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY) - strokeWidth

        rect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        canvas.drawArc(rect, startAngle, sweepAngle, false, paint)
    }

    fun startAnimation() {
        if (!isAnimating) {
            isAnimating = true
            object : CountDownTimer(animationDuration, 100) {
                override fun onTick(millisUntilFinished: Long) {
                    currentProgress = ((animationDuration - millisUntilFinished) / animationDuration.toFloat() * 100).toInt()
                    sweepAngle = (maxSweepAngle * currentProgress / 100).toFloat()
                    invalidate()
                }

                override fun onFinish() {
                    isAnimating = false
                    currentProgress = 100
                    sweepAngle = maxSweepAngle
                    invalidate()
                    loadingView.visibility = View.GONE
                }
            }.start()
        }
    }
}