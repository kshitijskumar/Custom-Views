package com.example.customviews.canvas

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.animation.ValueAnimator.*
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ThunderLoading(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var finalWidth = 0f
    private var finalHeight = 0f

    private var thunderX = 0f
    private var thunderY = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)

        finalHeight = when(MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentHeight.toFloat()
            MeasureSpec.AT_MOST -> minOf(parentHeight, 300).toFloat()
            else -> 300f
        }

        finalWidth = when(MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> parentWidth.toFloat()
            MeasureSpec.AT_MOST -> minOf(parentWidth, (finalHeight/2).toInt()).toFloat()
            else -> finalHeight/2
        }

        setMeasuredDimension(finalWidth.toInt(), finalHeight.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        drawThunderBolt()
        provideValueAnimator().start()
    }

    private val path = Path()
    private val blurPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        maskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.NORMAL)
        style = Paint.Style.STROKE
        strokeWidth = 15f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    private val solidPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.STROKE
        strokeWidth = 10f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas == null) return

        canvas.drawPath(path, blurPaint)
        canvas.drawPath(path, solidPaint)
    }

    private fun drawThunderBolt() {
        path.moveTo(finalWidth/2, finalHeight/8)
    }

    private fun provideValueAnimator(): ValueAnimator {
        val xVals = PropertyValuesHolder.ofFloat("x",finalWidth/2, finalWidth/3, 2*finalWidth/3, finalWidth/2)
        val yVals = PropertyValuesHolder.ofFloat("y", finalHeight/8, finalHeight/2, 7*finalHeight/16, 7*finalHeight/8)
        return ValueAnimator().apply {
            setValues(xVals, yVals)
            duration = 1000L
            repeatMode = REVERSE
            repeatCount = INFINITE
            addUpdateListener {
                thunderX = it.getAnimatedValue("x").toString().toFloat()
                thunderY = it.getAnimatedValue("y").toString().toFloat()
                path.lineTo(thunderX, thunderY)

                if(thunderY == 7*finalHeight/8) {
//                    path.reset()
//                    path.moveTo(finalWidth/2, finalHeight/8)
                    path.rewind()
                }
                invalidate()
            }
        }
    }
}