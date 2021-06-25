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
import android.view.animation.AccelerateDecelerateInterpolator

class TriangleDotsLoading(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var finalWidth = 0f
    private var finalHeight = 0f

    private var dot1X = 0f
    private var dot1Y = 0f
    private var dot2X = 0f
    private var dot2Y = 0f
    private var dot3X = 0f
    private var dot3Y = 0f

    private var dotRadius = 25f

    private val dot1Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.YELLOW
    }
    private val dot2Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }
    private val dot3Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)

        finalWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentWidth.toFloat()
            MeasureSpec.AT_MOST -> minOf(parentWidth, 200).toFloat()
            else -> 200f
        }
        finalHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentHeight.toFloat()
            MeasureSpec.AT_MOST -> minOf(parentHeight.toFloat(), (0.866 * finalWidth).toFloat())
            else -> (0.866 * finalWidth).toFloat()
        }

        setMeasuredDimension(finalWidth.toInt(), finalHeight.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        dot1X = finalWidth / 2
        dot1Y = dotRadius

        dot2X = dotRadius
        dot2Y = finalHeight - dotRadius

        dot3X = finalWidth - dotRadius
        dot3Y = finalHeight - dotRadius

        provideValuesAnimator().start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        canvas.drawCircle(dot1X, dot1Y, dotRadius, dot1Paint)
        canvas.drawCircle(dot2X, dot2Y, dotRadius, dot2Paint)
        canvas.drawCircle(dot3X, dot3Y, dotRadius, dot3Paint)
    }

    private fun provideValuesAnimator(): ValueAnimator {
        val dot1XProp = PropertyValuesHolder.ofFloat(
            "dot1x",
            finalWidth / 2,
            finalWidth - dotRadius,
            dotRadius,
            finalWidth / 2
        )
        val dot1YProp = PropertyValuesHolder.ofFloat(
            "dot1y",
            dotRadius,
            finalHeight - dotRadius,
            finalHeight - dotRadius,
            dotRadius
        )
        val dot2XProp = PropertyValuesHolder.ofFloat(
            "dot2x",
            dotRadius,
            finalWidth / 2,
            finalWidth - dotRadius,
            dotRadius
        )
        val dot2YProp = PropertyValuesHolder.ofFloat(
            "dot2y",
            finalHeight - dotRadius,
            dotRadius,
            finalHeight - dotRadius,
            finalHeight - dotRadius
        )
        val dot3XProp = PropertyValuesHolder.ofFloat(
            "dot3x",
            finalWidth - dotRadius,
            dotRadius,
            finalWidth / 2,
            finalWidth - dotRadius
        )
        val dot3YProp = PropertyValuesHolder.ofFloat(
            "dot3y",
            finalHeight - dotRadius,
            finalHeight - dotRadius,
            dotRadius,
            finalHeight - dotRadius
        )

        return ValueAnimator().apply {
            setValues(dot1XProp, dot1YProp, dot2XProp, dot2YProp, dot3XProp, dot3YProp)
            duration = 1200L
            repeatCount = INFINITE
            repeatMode = RESTART
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                dot1X = it.getAnimatedValue("dot1x").toString().toFloat()
                dot1Y = it.getAnimatedValue("dot1y").toString().toFloat()
                dot2X = it.getAnimatedValue("dot2x").toString().toFloat()
                dot2Y = it.getAnimatedValue("dot2y").toString().toFloat()
                dot3X = it.getAnimatedValue("dot3x").toString().toFloat()
                dot3Y = it.getAnimatedValue("dot3y").toString().toFloat()

                invalidate()
            }
        }
    }
}