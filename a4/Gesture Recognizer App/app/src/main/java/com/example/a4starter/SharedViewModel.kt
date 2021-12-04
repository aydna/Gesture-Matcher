package com.example.a4starter

import android.graphics.Bitmap
import android.graphics.Path
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import android.graphics.Point
import kotlin.collections.ArrayList

class SharedViewModel : ViewModel() {
    val desc: MutableLiveData<String> = MutableLiveData()
    val strokeGestures: MutableLiveData<ArrayList<Path>> = MutableLiveData<ArrayList<Path>>()
    val gesturesList: ArrayList<gestureInfo> = ArrayList()

    init {
        desc.value = "Shared model"
        //strokeGestures.value?.add(Path()) // empty path for illustration purposes
    }

    fun addStroke(currpath: Path, bitmap: Bitmap, name: String) {
        // do the path recognition later

        // handle library listview
        var currGesture = gestureInfo(currpath, bitmap, name)
        gesturesList.add(currGesture) // add to list
    }

    fun delStroke(pos: Int) {
        gesturesList.removeAt(pos)
    }

    // ... more methods added here
    class gestureInfo(path: Path, bitmap: Bitmap, name: String) {

        val path = path
        val bitmap = bitmap
        val name = name

    }

    // check if name exists
    fun nameExists(name: String): Boolean {
        for (gesture in gesturesList) {
            if (gesture.name == name) return true
        }
        return false
    }

    class Point(currx: Double, curry: Double) {
        var x = 0.0
        var y = 0.0

        init {
            x = currx
            y = curry
        }
    }

}

// use the drawing demo code provided (for drawing class and storing path)
// added CanvasView and GestureArrayAdapter (discussion 37:27)
// canvasview for path drawing etc. (probs add this view to addition fragment and others)
// Add drawable region for canvasview
// for layout, add the following xml files:
//  edit_popup_layout (this one not rly needed), gesture_item, and gesture_item_recognizer
// also use listview for recognizer matching
// can add our canvasView as xml for layout