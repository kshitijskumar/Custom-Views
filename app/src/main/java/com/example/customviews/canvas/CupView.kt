package com.example.customviews.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.abs


//Part3: We are trying to make a cup here using canvas and path
class CupView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        const val deltaX = 0.3f
    }

    //Part 3.1
    //we defined some variables, path will be used to draw on the canvas
    private var finalWidth = 0f
    private var finalHeight = 0f

    private val cupPath = Path()
    private val cap1Path = Path()
    private val cap2Path = Path()
    private val strawPath = Path()
    private val wavePath = Path()

    private var tiltStartY = 0f
    private var tiltEndY = 0f
    private var tiltStartX = 0f
    private var tiltEndX = 0f


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    private val cokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1976D2")
        style = Paint.Style.FILL
    }

    //Part 3.2
    //we are not changing the size of the view, so even in wrap_content, view would take the entire space.
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        finalWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        finalHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()

        Log.d("Cup", "onMeasure")

        tiltStartY = finalHeight / 2
        tiltEndY = finalHeight / 2
        tiltStartX = finalWidth / 6 + 18
        tiltEndX = 5 * finalWidth / 6 - 18
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        Log.d("Cup", "onSizechanged")

        drawBottomCup()
        drawCap1()
        drawCap2()
        drawStraw()
        drawWave()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        Log.d("Cup", "onDraw")
        canvas.drawPath(cupPath, paint)
        canvas.drawPath(cap1Path, paint)
        canvas.drawPath(cap2Path, paint)
        canvas.drawPath(strawPath, paint)
        canvas.drawPath(wavePath, cokePaint)
    }

    //Part3.3 path.moveto is like placing the pencil on the paper, from that point forward you draw.
    private fun drawBottomCup() {
        val startX = finalWidth / 6
        val startY = 3 * finalHeight / 8
        paint.pathEffect = CornerPathEffect(50f)
        cupPath.moveTo(startX, startY)
        cupPath.lineTo(startX + 50, 7 * finalHeight / 8)
        cupPath.lineTo(5 * startX - 70, 7 * finalHeight / 8)
        cupPath.lineTo(5 * startX, startY)

        //additionally we can also use `path.close`, this lets us use the same path from some place else as well, just like a new path
        //but it can only be used, if your endpoint meets the start point, since that was not my requirement, I skipped it.
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

        //start angle is like from where the arc would start to draw.
        //0deg -> 3'O clock in watch ; 90deg -> 6'O ; 180 -> 9'O ; 270deg -> 12'O
        cap2Path.arcTo(
            startX + 20,
            startY - 120,
            startX + 200,
            startY + 30,
            180f,
            90f,
            true
        )
        cap2Path.lineTo(5 * startX - 100, startY - 120)
        cap2Path.arcTo(
            5 * startX - 200,
            startY - 120,
            5 * startX - 20,
            startY + 30,
            0f,
            -90f,
            true
        )
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

    private fun drawWave() {
        //reset the path, so that you draw fresh this time
        wavePath.reset()
        wavePath.moveTo(tiltStartX, tiltStartY)
        wavePath.lineTo(tiltEndX, tiltEndY)
        cokePaint.pathEffect = CornerPathEffect(20f)
        wavePath.lineTo(5 * finalWidth / 6 - 73, 7 * finalHeight / 8 - 4)
        wavePath.lineTo(finalWidth / 6 + 52, 7 * finalHeight / 8 - 4)
        wavePath.lineTo(tiltStartX, tiltStartY)

    }

    fun tiltCoke(angle: Float) {
        tiltStartY = finalHeight / 2
        tiltEndY = finalHeight / 2
        tiltStartX = finalWidth / 6 + 18
        tiltEndX = 5 * finalWidth / 6 - 18
        val topLimit = 3 * finalHeight / 8
        if (angle > 0) {
            var i = 0
            while (i <= angle.toInt() && tiltStartY >= topLimit) {
                tiltStartY -= 5f
                tiltEndY += 5f
                tiltStartX -= deltaX
                tiltEndX -= deltaX
                i++
            }
        } else {
            var i = 0
            while (i <= abs(angle).toInt() && tiltEndY >= topLimit) {
                tiltStartY += 5f
                tiltEndY -= 5f
                tiltStartX += 0.3f
                tiltEndX += 0.3f
                i++
            }
        }
        //draw the new path
        drawWave()
        //invoke onDraw
        invalidate()
    }
}