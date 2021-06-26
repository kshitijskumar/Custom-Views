package com.example.customviews.canvas

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.RESTART
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.cos
import kotlin.math.sin

class Sharingan(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var finalHeight = 0f
    private var finalWidth = 0f

    private var outerRadius = 0f
    private var innerRadius = 0f

    private var dot1X = 0f
    private var dot1Y = 0f

    private var dot2X = 0f
    private var dot2Y = 0f

    private var dot3X = 0f
    private var dot3Y = 0f

    private val redEyePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 8f
        color = Color.parseColor("#af1313")
    }

    private val innerBlackLoop = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.BLACK
    }

    private val blackFilledPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    private val pinkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#d04040")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)

        finalWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentWidth.toFloat()
            MeasureSpec.AT_MOST -> minOf(parentWidth.toFloat(), 150f)
            else -> 150f
        }
        finalHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentHeight.toFloat()
            MeasureSpec.AT_MOST -> minOf(parentHeight.toFloat(), finalWidth)
            else -> finalWidth
        }

        setMeasuredDimension(finalWidth.toInt(), finalHeight.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        outerRadius = finalWidth / 2 - 10
        innerRadius = 4 * outerRadius / 6

//        updatePos(30.0)
        provideValueAnimator().start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        canvas.drawCircle(finalWidth / 2, finalHeight / 2, outerRadius, redEyePaint)
        canvas.drawCircle(finalWidth / 2, finalHeight / 2, innerRadius, innerBlackLoop)
        canvas.drawCircle(finalWidth / 2, finalHeight / 2, 3 * innerRadius / 4, pinkPaint)
        canvas.drawCircle(finalWidth / 2, finalHeight / 2, outerRadius / 4, blackFilledPaint)

        canvas.drawCircle(dot1X, dot1Y, 10f, blackFilledPaint)
        canvas.drawCircle(dot2X, dot2Y, 10f, blackFilledPaint)
        canvas.drawCircle(dot3X, dot3Y, 10f, blackFilledPaint)

    }

    private fun calculateNewPos(angle: Double, valueCalc: (x: Float, y: Float) -> Unit) {
        val radAngle = Math.toRadians(angle)
        val x = (finalWidth / 2 + cos(radAngle) * innerRadius).toFloat()
        val y = (finalHeight / 2 + sin(radAngle) * innerRadius).toFloat()

        valueCalc.invoke(x, y)
    }

    private fun updatePos(angle1: Double) {
        calculateNewPos(angle1 % 360) { x, y ->
            dot1X = x
            dot1Y = y
        }
        calculateNewPos((angle1 + 120) % 360) { x, y ->
            dot2X = x
            dot2Y = y
        }
        calculateNewPos((angle1 + 240) % 360) { x, y ->
            dot3X = x
            dot3Y = y
        }
    }

    private fun provideValueAnimator(): ValueAnimator {
        val angles = List(361) { i -> ((i + 30) % 360).toFloat() }.toTypedArray().toFloatArray()
        val angleProps = PropertyValuesHolder.ofFloat("angles", *angles)

        return ValueAnimator().apply {
            setValues(angleProps)
            duration = 1500L
            repeatMode = RESTART
            repeatCount = INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                updatePos(it.animatedValue.toString().toDouble())
                invalidate()
            }
        }

    }
}