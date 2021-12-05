package com.example.a4starter

import android.graphics.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Math.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.atan2
import kotlin.math.pow

class SharedViewModel : ViewModel() {
    val desc: MutableLiveData<String> = MutableLiveData()
    val strokeGestures: MutableLiveData<ArrayList<Path>> = MutableLiveData<ArrayList<Path>>()
    val gesturesList: ArrayList<gestureInfo> = ArrayList()

    val numPathPoints: Int = 128

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


    // ... more methods added here
    class gestureInfo(path: Path, bitmap: Bitmap?, name: String) {

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

    // Algorithm to find gesture matches

    // redraw path with 128 points for matching
    fun redrawPath(oldPath: Path): Path {
        var newPath = Path()
        var pm = PathMeasure(oldPath, false)

        var dist = pm.length // max distance
        var currCoords: FloatArray = FloatArray(2)

        pm.getPosTan(0.0f, currCoords, null)
        newPath.moveTo(currCoords[0], currCoords[1]) // initialize newPath

        val distPerPoint = dist/(numPathPoints - 1) // we do -1 because of the beg. point
        for (i in 1 until numPathPoints) {
            pm.getPosTan(distPerPoint*i, currCoords, null)
            newPath.lineTo(currCoords[0], currCoords[1]) // updated coords
        }
        return newPath
    }

    // move path to be set about the top left of canvas
    fun translatePath(redrawnPath: Path): Path {
        val centroid = getCentroid(redrawnPath)

        val matrix = Matrix()
        matrix.setTranslate(-centroid[0], -centroid[1]) // assume centroid is positive
        redrawnPath.transform(matrix) // set centroid about the origin
        return redrawnPath
    }

    // rotate path such that centroid-initial gesture angle is 0
    fun rotatePath(redrawnPath: Path): Path {
        val centroid = getCentroid(redrawnPath)
        // get beginning point
        var pm = PathMeasure(redrawnPath, false)
        var begCoords: FloatArray = FloatArray(2)
        pm.getPosTan(0.0f, begCoords, null)

        val angle: Double = toDegrees(atan2(begCoords[1].toDouble() - centroid[1], begCoords[0].toDouble() - centroid[0]))

        val matrix: Matrix = Matrix()
        matrix.postRotate(-angle.toFloat(), centroid[0], centroid[1])
        redrawnPath.transform(matrix)
        return redrawnPath
    }

    // scale
    fun scalePath(redrawnPath: Path): Path {
        val centroid = getCentroid(redrawnPath)
        var pm = PathMeasure(redrawnPath, false)
        var coords: FloatArray = FloatArray(2)
        var dist = pm.length/(numPathPoints - 1) // distance between points
        var minX = 0.0f
        var minY = 0.0f
        var maxX = 0.0f
        var maxY = 0.0f

        for (i in 0 until numPathPoints) {
            pm.getPosTan(dist*i, coords, null)
            if (coords[0] > maxX) {
                maxX = coords[0] // update
            }
            else if (coords[0] < minX) {
                minX = coords[0]
            }
            // same for y
            if (coords[1] > maxY) {
                maxY = coords[1]
            }
            else if (coords[1] < minY) {
                minY = coords[1]
            }
        }

        var diff = 0.0f
        if ((maxX - minX) > (maxY - minY)) {
            // x diff is greater. use that as scaling factor
            diff = maxX - minX
        }
        else {
            // y diff is greater
            diff = maxY - minY
        }

        val scale: Float = 100f/diff // use 100 as the scale bound
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        redrawnPath.transform(matrix)
        return redrawnPath
    }

    private fun getCentroid(path: Path): FloatArray {
        var pm = PathMeasure(path, false)

        var sumx: Float = 0.0f
        var sumy: Float = 0.0f
        var currCoords: FloatArray = FloatArray(2)
        val distPerPoint = pm.length/(numPathPoints - 1) // we do -1 because of the beg. point

        for (i in 0 until numPathPoints) {
            pm.getPosTan(distPerPoint*i, currCoords, null)
            sumx += currCoords[0]
            sumy += currCoords[1]
        }
        val centroidx = sumx/numPathPoints
        val centroidy = sumy/numPathPoints
        currCoords[0] = centroidx
        currCoords[1] = centroidy

        return currCoords
    }

    // returns dist (similarity between 2 paths (strokes))
    fun comparePath(p1: Path, p2: Path): Float {
        val pm1 = PathMeasure(p1, false)
        val pm2 = PathMeasure(p2, false)
        var dist: Float = 0f
        var c1 = FloatArray(2) // coords for pm1
        var c2 = FloatArray(2) // coords for pm2

        for (i in 0 until numPathPoints) {
            pm1.getPosTan(pm1.length /127*i, c1, null)
            pm2.getPosTan(pm2.length /127*i, c2, null)

            dist += kotlin.math.sqrt((c1[0] - c2[0]).pow(2) + (c1[1] - c2[1]).pow(2))
        }
        return dist
    }

    // wrapper for all transformations
    fun transformPath(oldPath: Path): Path {
        return scalePath(translatePath(rotatePath(redrawPath(oldPath))))
    }

}
