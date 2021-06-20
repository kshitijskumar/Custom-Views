package com.example.customviews.canvas

import android.content.Context
import android.util.AttributeSet
import android.view.View

//I've divided the various functions in Parts in which I feel you should read, it may be better for understanding (as it was for me)
class CustomView1(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        private const val TAG = "CustomView1"
    }

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
        val finalWidth = when (widthMode) {
            //this mode is when you set layout_width as match_parent or some definite dp, eg. 300dp
            //then we don't want to change the size
            MeasureSpec.EXACTLY -> widthSize
            //this mode is when you se the width as wrap content, here we set the desired width
            //either the size we want or the widthSize we extracted from specifications (minimum of these two is chosen)
            MeasureSpec.AT_MOST -> Math.min(200, widthSize)
            //this is for mode UNSPECIFIED which is used by Android system when it queries for the size
            else -> 200
        }

        val finalHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(200, heightSize)
            else -> 200
        }

        //now we set the dimension we specified for our view
        setMeasuredDimension(finalWidth, finalHeight)

        //Note: If you do not specify the width of your view and simply use widthSize in setMeasuredDimension,
        //then even if you set the width as wrap_content, your view would  take the entire width of the parent
        //hence if you want some dedicated size in case of wrap_content, do change the width on mode AT_MOST
    }
}