package com.example.customviews.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CupView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var finalWidth = 0f
    var finalHeight = 0f

    private val cupPath = Path()
    private val cap1Path = Path()
    private val cap2LeftPath = Path()
    private val cap2RightPath = Path()
    private val cap2Line = Path()
    private val strawPath = Path()


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        finalWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        finalHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        drawBottomCup()
        canvas.drawPath(cupPath, paint)
        drawCap1()
        canvas.drawPath(cap1Path, paint)
        drawCap2()
        canvas.drawPath(cap2LeftPath, paint)
        canvas.drawPath(cap2RightPath, paint)
        canvas.drawPath(cap2Line, paint)
        drawStraw()
        canvas.drawPath(strawPath, paint)
    }

    private fun drawBottomCup() {
        val startX = finalWidth / 6
        val startY = 3 * finalHeight / 8
        paint.pathEffect = CornerPathEffect(50f)
        cupPath.moveTo(startX, startY)
        cupPath.lineTo(startX + 50, 7 * finalHeight / 8)
        cupPath.lineTo(5 * startX - 70, 7 * finalHeight / 8)
        cupPath.lineTo(5 * startX, startY)
    }

    private fun drawCap1() {
        val startX = finalWidth / 6
        val startY = 3 * finalHeight / 8

        cap1Path.addRoundRect(
            startX - 30,
            startY - 50,
            5 * startX + 35,
            startY - 10,
            50f,
            0f,
            Path.Direction.CW
        )
    }

    private fun drawCap2() {
        val startX = finalWidth / 6
        val startY = 3 * finalHeight / 8

        cap2LeftPath.arcTo(
            startX + 20,
            startY - 120,
            startX + 200,
            startY + 30,
            180f,
            90f,
            true
        )
        cap2RightPath.arcTo(
            5 * startX - 200,
            startY - 120,
            5 * startX - 20,
            startY + 30,
            0f,
            -90f,
            true
        )

        cap2Line.moveTo(startX + 110, startY - 120)
        cap2Line.lineTo(5 * startX - 110, startY - 120)
    }

    private fun drawStraw() {
        val centerX = finalWidth / 2
        val startY = 3 * finalHeight / 8 - 120

        paint.pathEffect = CornerPathEffect(20f)

        strawPath.moveTo(centerX - 40, startY)
        strawPath.lineTo(centerX + 20, startY - 350)
        strawPath.lineTo(centerX + 250, startY - 400)
        strawPath.lineTo(centerX + 240, startY - 320)
        strawPath.lineTo(centerX + 80, startY - 290)
        strawPath.lineTo(centerX + 30, startY)
    }
}