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


//Part4. Loading dots
class DotLoading(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var finalHeight = 0f
    private var finalWidth = 0f

    private var centerDot1X = 0f
    private var centerDot1Y = 0f
    private var centerDot2X = 0f
    private var centerDot2Y = 0f
    private var centerDot3X = 0f
    private var centerDot3Y = 0f

    private var deltaMax = 0f
    private var dotRadius = 15f

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.GRAY
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)

        val wrapWidth = 6*dotRadius + 25*4
        finalWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentWidth.toFloat()
            MeasureSpec.AT_MOST -> parentWidth.coerceAtMost(wrapWidth.toInt()).toFloat()
            else -> wrapWidth
        }
        finalHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentHeight.toFloat()
            MeasureSpec.AT_MOST -> parentHeight.coerceAtMost(200).toFloat()
            else -> 200f
        }

        setMeasuredDimension(finalWidth.toInt(), finalHeight.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerDot2X = finalWidth / 2
        centerDot1X = centerDot2X - 2*dotRadius - 25
        centerDot3X = centerDot2X + 2*dotRadius + 25
        centerDot1Y = finalHeight / 2
        centerDot2Y = finalHeight / 2
        centerDot3Y = finalHeight / 2

        deltaMax = finalHeight / 8

        provideAnimator(0L) {
            centerDot3Y = it
        }.start()

        provideAnimator(150L) {
            centerDot2Y = it
        }.start()

        provideAnimator(300L) {
            centerDot1Y = it
        }.start()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        canvas.drawCircle(centerDot1X, centerDot1Y, dotRadius, dotPaint)
        canvas.drawCircle(centerDot2X, centerDot2Y, dotRadius, dotPaint)
        canvas.drawCircle(centerDot3X, centerDot3Y, dotRadius, dotPaint)
    }

    private fun provideAnimator(
        delay: Long,
        updatedVals: (animatedVal: Float) -> Unit
    ): ValueAnimator {
        val dot1Property = PropertyValuesHolder.ofFloat(
            "translationY",
            finalHeight / 2,
            finalHeight / 2 - deltaMax,
            finalHeight / 2,
            finalHeight / 2 + deltaMax,
            finalHeight / 2
        )
        return ValueAnimator().apply {
            duration = 1000L
            setValues(dot1Property)
            repeatCount = INFINITE
            repeatMode = RESTART
            interpolator = LinearInterpolator()
            startDelay = delay
            addUpdateListener {
                updatedVals.invoke(it.animatedValue.toString().toFloat())
                invalidate()
            }
        }
    }
}