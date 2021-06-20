package com.example.customviews.canvas

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.FloatProperty
import android.view.View

class AnimatedView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {

    var rectRadius = 0f

    val path = Path()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLUE
        strokeWidth = 20f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        canvas.drawRoundRect(100f, 100f, 400f, 400f, rectRadius, rectRadius, paint)
    }

    private val rectValueAnimator = ValueAnimator().apply {
        duration = 2000L
        setFloatValues(0f, 200f)
        this.addUpdateListener {
            rectRadius = it.animatedValue.toString().toFloat()
            invalidate()
        }
    }.start()

    private val lineDrawing = ValueAnimator().apply {
        duration = 2000L
    }

}