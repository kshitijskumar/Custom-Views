package com.example.customviews.canvas

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.min

//I've marked the functions in which order you should try to understand
class TimerView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var finalWidth = 0f
    private var finalHeight = 0f

    private var sweepAngle = 360f

    private var timerRadius = 0f

    private var timerCx = 0f
    private var timerCy = 0f

    private var timeInText = "Start"

    private lateinit var rect: RectF

    //to animate any value, we use ValueAnimator class
    private val timerAnimator = ValueAnimator().apply {
        setFloatValues(-360f, 0f)
        interpolator = LinearInterpolator()
        addUpdateListener {
            sweepAngle = it.animatedValue.toString().toFloat()
            timeInText = millisToSeconds(it.currentPlayTime).toString()
            invalidate()
        }
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 10f
        color = Color.WHITE
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }

    private val timerBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }

    private val timerCompletePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.CYAN
        style = Paint.Style.FILL
    }

    //1
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        finalWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> parentWidth.toFloat()
            MeasureSpec.AT_MOST -> min(
                3 * parentWidth / 4,
                3 * parentHeight / 4
            ).toFloat()
            else -> 400f
        }
        finalHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> parentHeight.toFloat()
            MeasureSpec.AT_MOST -> min(
                3 * parentWidth / 4,
                3 * parentHeight / 4
            ).toFloat()
            else -> 400f
        }
        setMeasuredDimension(finalWidth.toInt(), finalHeight.toInt())
    }

    //2
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        setupTimerConfig()
        rect = RectF(
            timerCx - timerRadius, timerCy - timerRadius,
            timerCx + timerRadius, timerCy + timerRadius
        )
    }

    private fun setupTimerConfig() {
        timerRadius = calculateRadius(finalWidth, finalHeight)
        timerCx = finalWidth / 2
        timerCy = finalHeight / 2
        textPaint.textSize = 100f
    }

    //3
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        drawTimer(canvas)
    }

    private fun drawTimer(canvas: Canvas) {
        canvas.drawCircle(timerCx, timerCy, timerRadius, timerBgPaint)
        canvas.drawArc(rect, 270f, sweepAngle, true, timerCompletePaint)

        canvas.drawText(timeInText, timerCx, timerCy, textPaint)
    }

    private fun calculateRadius(width: Float, height: Float): Float {

        val xDiameter = width - 2 * width / 20
        val yDiameter = height - 2 * height / 20

        return min(xDiameter, yDiameter) / 2

    }

    fun startTimer(timeInMillis: Long = 0L) {
        timerAnimator.duration = timeInMillis
        timerAnimator.start()
    }

    private fun millisToSeconds(timeInMillis: Long): Int {
        return (timeInMillis / 1000).toInt()
    }

}