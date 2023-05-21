package com.polzzak_android.presentation.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.polzzak_android.R

class SemiCircleProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val rectF = RectF()
    private val progressPaint = Paint()
    private val fullProgressPaint = Paint()

    private val arcStartAngle = -220f   // 시작 각도
    private var arcSweepAngle = 0f      // 유저 진행도
    private val arcEndAngle = 260f      // 끝 각도

    private var radius = 1f
    private val strokeWidth = 60f        //

    init {
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeJoin = Paint.Join.ROUND
        progressPaint.strokeCap = Paint.Cap.ROUND

        fullProgressPaint.style = Paint.Style.STROKE
        fullProgressPaint.strokeJoin = Paint.Join.ROUND
        fullProgressPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // set arc
        val centerX = width.toFloat() / 2
        val centerY = height.toFloat() / 2
        radius = (Math.min(width, height) / 2).toFloat()

        // draw arc
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        // draw stroke
        progressPaint.strokeWidth = strokeWidth
        progressPaint.color = resources.getColor(R.color.primary)
        fullProgressPaint.strokeWidth = strokeWidth
        fullProgressPaint.color = resources.getColor(R.color.primary_100)
        rectF.set(
            centerX - radius + strokeWidth / 2,
            centerY - radius + strokeWidth / 2,
            centerX + radius - strokeWidth / 2,
            centerY + radius - strokeWidth / 2
        )
        canvas.drawArc(rectF, arcStartAngle, arcEndAngle, false, fullProgressPaint)
        canvas.drawArc(rectF, arcStartAngle, arcSweepAngle, false, progressPaint)
    }

    fun setAnimation(endValue: Float) {
        val startValue = arcSweepAngle

        ValueAnimator.ofFloat(startValue, endValue).apply {
            this.duration = 1000L
            addUpdateListener { animation ->
                arcSweepAngle = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

}