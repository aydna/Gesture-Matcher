package com.example.a4starter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.graphics.Point
import androidx.annotation.ColorRes
import androidx.lifecycle.ViewModelProvider
import android.graphics.drawable.Drawable

import android.graphics.Bitmap
import androidx.core.view.drawToBitmap


class CanvasView: View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}


    // have to add the canvas view (drawing view) here. includes path points

    //var currPathPoints: ArrayList<SharedViewModel.Point> = ArrayList()
    var currPath: Path? = null
    var paths: ArrayList<Path?> = ArrayList()
    var currX: Float = 0.0F
    var currY: Float = 0.0F
    //var pathBitmap: Bitmap? = this.drawToBitmap()

    // drawing
    var paintbrush = Paint(Color.BLACK)

    init {
        paintbrush.style = Paint.Style.STROKE
        paintbrush.strokeWidth = 8F

    }

    override fun setOnTouchListener(l: OnTouchListener?) {
        super.setOnTouchListener(l)
    }

    // on touch of view do this
    override fun onTouchEvent(event: MotionEvent): Boolean {
        currX = event.x
        currY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downPress(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                dragTouch(event.x, event.y)
            }

            MotionEvent.ACTION_UP -> {
                liftPress()
                paths.add(currPath)
            }
        }
        return true
    }

    fun downPress(x: Float, y: Float) {
        //add path points to sharedViewModel path array
        //currPathPoints = ArrayList()
        //urrPathPoints.add(SharedViewModel.Point(x.toDouble(), y.toDouble())) // add this to points array in model
        currPath = Path()
        currPath!!.moveTo(x, y)
        currX = x
        currY = y
        invalidate()
    }

    fun dragTouch(x: Float, y: Float) {
        // add path points when dragging cursor on canvas
        val dx = Math.abs(x - currX)
        val dy = Math.abs(x - currY)

        //set a tolerance. eg. n = 128 (distance between putting in a path point)
        //currPathPoints.add(SharedViewModel.Point(x.toDouble(), y.toDouble()))
        currPath?.lineTo(x, y)
        currX = x
        currY = y
        invalidate()
    }

    fun liftPress() {
        //currPathPoints.add(SharedViewModel.Point(currX.toDouble(), currY.toDouble()))
        currPath?.lineTo(currX, currY)
    }

    // clear canvas
    fun clear() {
        currPath = null // reset path
        invalidate() // reset canvas
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /* set canvas background here */
        canvas.drawColor(Color.LTGRAY)
        //currPath?.let { canvas.drawPath(it, paintbrush) }
        currPath?.let { canvas.drawPath(it, paintbrush) }
    }

}