package com.example.a4starter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

// this view is in place for the library listview's gesture image
class BitmapView: View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    var paintbrush = Paint(Color.BLACK)
    var currPath: Path? = null

    init {
        // initialize same paintbrush parameters as the canvasView
        paintbrush.style = Paint.Style.STROKE
        paintbrush.strokeWidth = 6F
    }

    // set the currpath to draw
    fun setPath(path: Path) {
        currPath = path
        invalidate()
    }

    override fun setOnTouchListener(l: OnTouchListener?) {
        super.setOnTouchListener(l)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        currPath?.let {
            canvas?.drawPath(it, paintbrush)
        }
    }

}