package com.example.customviews.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

//I've divided the various functions in Parts in which I feel you should read, it may be better for understanding (as it was for me)
//We've multiple options for constructors in a view, we've chosen this constructor rather than the one with only context, is because we want
//use this view from xml as well, where to set width height and everything we need AttributeSet
class CustomView1(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        private const val TAG = "CustomView1"
    }

    private var finalWidth = 0
    private var finalHeight = 0

    //Part 1
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //we can remove this super method, but then we have to make sure that we call setMeasuredDimension
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // we get the width and height in this way. we are provided the measure specification(params of onMeasure), out of which
        // we get the mode and size
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        //there are 3 kinds of mode, EXACT, AT_MOST, UNSPECIFIED, we extract this info from the measure spec given to us
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        //we set the width of our custom view
        finalWidth = when (widthMode) {
            //this mode is when you set layout_width as match_parent or some definite dp, eg. 300dp
            //then we don't want to change the size
            MeasureSpec.EXACTLY -> widthSize
            //this mode is when you se the width as wrap content, here we set the desired width
            //either the size we want or the widthSize we extracted from specifications (minimum of these two is chosen)
            MeasureSpec.AT_MOST -> Math.min(400, widthSize)
            //this is for mode UNSPECIFIED which is used by Android system when it queries for the size
            else -> 400
        }

        finalHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(400, heightSize)
            else -> 400
        }

        //now we set the dimension we specified for our view
        setMeasuredDimension(finalWidth, finalHeight)

        //Note: If you do not specify the width of your view and simply use widthSize in setMeasuredDimension,
        //then even if you set the width as wrap_content, your view would  take the entire width of the parent
        //hence if you want some dedicated size in case of wrap_content, do change the width on mode AT_MOST
    }

    //Part 2
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private val color1 = Color.CYAN

    //try to avoid as much as calculation out from the onDraw method, because this is called multiple times
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw called")
        if (canvas == null) return
//        drawLine(canvas)
//        drawDiagonals(canvas)
//        drawDiagonalFlower(canvas)
        drawTriangle()
        canvas.drawPath(path, paint)
    }

    //Part2.1
    private fun drawLine(canvas: Canvas) {
        //we draw a horizontal line on top of our view
        paint.color = color1
        canvas.drawLine(0f, 0f, (finalWidth / 2).toFloat(), 0f, paint)
    }

    //Part2.2
    private fun drawDiagonals(canvas: Canvas) {
        paint.color = color1
        canvas.drawLine(0f, 0f, finalWidth.toFloat(), finalHeight.toFloat(), paint)
        canvas.drawLine(0f, finalHeight.toFloat(), finalWidth.toFloat(), 0f, paint)
    }

    //Part2.3
    private fun drawDiagonalFlower(canvas: Canvas) {
        drawDiagonals(canvas)
        canvas.drawCircle(
            finalWidth.toFloat() / 2,
            finalHeight.toFloat() / 2,
            finalHeight.toFloat() / 5,
            paint
        )
    }

    //Part2.4
    private val path = Path()

    private fun drawTriangle() {
        val initialX = finalWidth.toFloat() / 5;
        val initialY = 0f

        paint.color = color1

        path.apply {
            moveTo(initialX, initialY)
            lineTo(finalWidth.toFloat() / 2, finalHeight.toFloat())
            lineTo(4 * finalWidth.toFloat() / 5, 0f)
            lineTo(initialX, initialY)
            close()
        }

    }
}