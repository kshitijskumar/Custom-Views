package com.example.customviews.canvas

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.animation.ValueAnimator.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class CircleWaveLoading(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var finalHeight = 0f
    private var finalWidth = 0f

    private var boundaryCircleRadius = 0f

    private var pt1Y = 0f
    private var pt2Y = 0f

    private var maxHeightDiff = 0f

    private val wavePath = Path()
    private val bgWavePath = Path()

    private val boundaryCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 10f
    }

    private var wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#0288D1")
    }
    private var bgWavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#2196F3")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)

        finalWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentWidth.toFloat()
            MeasureSpec.AT_MOST -> minOf(parentWidth, 125).toFloat()
            else -> 125f
        }

        finalHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentHeight.toFloat()
            MeasureSpec.AT_MOST -> minOf(parentHeight.toFloat(), finalWidth)
            else -> finalWidth
        }

        boundaryCircleRadius = finalWidth / 2 - 10
        maxHeightDiff = finalHeight / 8

        setMeasuredDimension(finalWidth.toInt(), finalHeight.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        drawWavePath(finalHeight / 4, 3 * finalHeight / 4, wavePath)
        drawWavePath(3 * finalHeight / 4, finalHeight / 4, bgWavePath)
        provideValueAnimator().start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        canvas.drawPath(bgWavePath, bgWavePaint)
        canvas.drawPath(wavePath, wavePaint)
        canvas.drawCircle(
            finalWidth / 2,
            finalHeight / 2,
            boundaryCircleRadius,
            boundaryCirclePaint
        )

    }

    private fun drawWavePath(y1: Float, y2: Float, path: Path) {
        path.reset()
        path.moveTo(10f, finalHeight / 2)
        path.cubicTo(
            finalWidth / 4,
            y1,
            3 * finalWidth / 4,
            y2,
            finalWidth - 10,
            finalHeight / 2
        )
        path.addArc(10f, 10f, finalWidth - 10, 2 * boundaryCircleRadius + 10, 0f, 180f)
    }

    private fun provideValueAnimator(): ValueAnimator {
        val y1Props = PropertyValuesHolder.ofFloat(
            "y1",
            finalHeight / 4,
            finalHeight / 2,
            3 * finalHeight / 4,
            finalHeight / 2,
            finalHeight / 4
        )
        val y2Props = PropertyValuesHolder.ofFloat(
            "y2",
            3 * finalHeight / 4,
            finalHeight / 2,
            finalHeight / 4,
            finalHeight / 2,
            3 * finalHeight / 4
        )

        return ValueAnimator().apply {
            duration = 2000L
            repeatMode = REVERSE
            repeatCount = INFINITE
            setValues(y1Props, y2Props)
            addUpdateListener {
                pt1Y = it.getAnimatedValue("y1").toString().toFloat()
                pt2Y = it.getAnimatedValue("y2").toString().toFloat()

                drawWavePath(pt1Y, pt2Y, wavePath)
                drawWavePath(pt2Y, pt1Y, bgWavePath)
                invalidate()
            }
        }
    }
}