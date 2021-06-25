package com.example.customviews.canvas

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BounceLoading(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var finalHeight = 0f
    private var finalWidth = 0f

    private var dotRadius = 0f

    private var cx1 = 0f
    private var cx3 = 0f
    private var cy2 = 0f

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.DKGRAY
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)

        finalWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentWidth.toFloat()
            MeasureSpec.AT_MOST -> minOf(parentWidth.toFloat(), 125f)
            else -> 125f
        }
        finalHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentHeight.toFloat()
            MeasureSpec.AT_MOST -> minOf(parentHeight.toFloat(), finalWidth)
            else -> finalWidth
        }

        dotRadius = finalWidth / 12
        cx1 = finalWidth / 6
        cx3 = finalWidth - finalWidth / 6
        cy2 = finalHeight / 2
        setMeasuredDimension(finalWidth.toInt(), finalHeight.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        provideValueAnimator().start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        canvas.drawCircle(cx1, finalHeight / 2, dotRadius, dotPaint)
        canvas.drawCircle(finalWidth / 2, cy2, dotRadius, dotPaint)
        canvas.drawCircle(cx3, finalHeight / 2, dotRadius, dotPaint)
    }

    private fun provideValueAnimator(): ValueAnimator {
        val cx1Props = PropertyValuesHolder.ofFloat("cx1", finalWidth / 6, 5 * finalWidth / 6)
        val cx3Props = PropertyValuesHolder.ofFloat("cx3", 5 * finalWidth / 6, finalWidth / 6)
        val cy2Props =
            PropertyValuesHolder.ofFloat("cy2", finalHeight / 2, finalHeight / 4, finalHeight / 2)

        return ValueAnimator().apply {
            duration = 2000L
            repeatMode = REVERSE
            repeatCount = INFINITE
            setValues(cx1Props, cx3Props, cy2Props)
            addUpdateListener {
                cx1 = it.getAnimatedValue("cx1").toString().toFloat()
                cx3 = it.getAnimatedValue("cx3").toString().toFloat()
                cy2 = it.getAnimatedValue("cy2").toString().toFloat()

                invalidate()
            }
        }
    }

}