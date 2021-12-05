package com.example.a4starter

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlin.math.max
import kotlin.math.min

class HomeFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {

        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        //mViewModel!!.desc.observe(viewLifecycleOwner, { s:String -> textView.text = "$s - Recognition" })
        //mViewModel!!.strokeGestures.observe(viewLifecycleOwner, { s:ArrayList<Path> -> textView.text = "stroke count: ${s.size}"})

        val canvasView: CanvasView = root.findViewById(R.id.canvasView2)
        val bitmapView6: ImageView = root.findViewById(R.id.imageView2)
        val bitmapView7: ImageView = root.findViewById(R.id.imageView3)
        val bitmapView8: ImageView = root.findViewById(R.id.imageView4)
        val recognizeButton: Button = root.findViewById(R.id.button4)
        val clearButton: Button = root.findViewById(R.id.button6)
        val tv6: TextView = root.findViewById(R.id.textView6)
        val tv7: TextView = root.findViewById(R.id.textView7)
        val tv8: TextView = root.findViewById(R.id.textView8)

        recognizeButton.setOnClickListener() {
            // process algorithm

            // check for no drawn gesture and empty library
            if (canvasView.currPath == null) {
                val nullAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                nullAlertBuilder.setTitle("Invalid Operation")
                nullAlertBuilder.setMessage("There is no drawn gesture")

                val nullAlert: AlertDialog = nullAlertBuilder.create()
                nullAlert.show()
                return@setOnClickListener
            }
            else if (mViewModel!!.gesturesList.size == 0) {
                // nothing to match
                val nullAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                nullAlertBuilder.setTitle("Library is empty")
                nullAlertBuilder.setMessage("Please add gesture(s) to recognize")

                val nullAlert: AlertDialog = nullAlertBuilder.create()
                nullAlert.show()
                return@setOnClickListener
            }

            val gestures = mViewModel!!.gesturesList

            var topGesturesDist: FloatArray = FloatArray(3) // the top 3 gestures, in index order
            topGesturesDist[0] = Float.MAX_VALUE
            topGesturesDist[1] = Float.MAX_VALUE
            topGesturesDist[2] = Float.MAX_VALUE

            var topGestures: ArrayList<SharedViewModel.gestureInfo> = ArrayList()
            val dummyPath = Path()
            val dummyBitmap: Bitmap? = null
            for (i in 0..2) topGestures.add(SharedViewModel.gestureInfo(dummyPath, dummyBitmap, "")) // initialize

            val currPath: Path? = canvasView.currPath?.let { it1 -> mViewModel!!.transformPath(it1) } // get transformed path

            // compare distances
            for (gesture in gestures) {
                // set up new paths
                val tmpPath = mViewModel!!.transformPath(gesture.path)
                var currDist: Float = Float.MAX_VALUE

                if (currPath != null) {
                    currDist = mViewModel!!.comparePath(currPath, tmpPath)
                }

                // 1st place
                if (currDist < topGesturesDist[0]) {
                    // update rankings
                    topGesturesDist[2] = topGesturesDist[1]
                    topGesturesDist[1] = topGesturesDist[0]
                    topGesturesDist[0] = currDist

                    topGestures[2] = topGestures[1]
                    topGestures[1] = topGestures[0]
                    topGestures[0] = gesture
                }
                // 2nd place
                else if (currDist < topGesturesDist[1]) {
                    topGesturesDist[2] = topGesturesDist[1]
                    topGesturesDist[1] = currDist

                    topGestures[2] = topGestures[1]
                    topGestures[1] = gesture
                }
                // 3rd place
                else if (currDist < topGesturesDist[2]) {
                    topGesturesDist[2] = currDist
                    topGestures[2] = gesture
                }
            }

            // set up view
            for (i in 0 until min(gestures.size, 3)) {
                if (i == 0) {
                    bitmapView6.setImageBitmap(topGestures[i].bitmap) // set image
                    tv6.text = "1. " + topGestures[i].name
                }
                else if (i == 1) {
                    bitmapView7.setImageBitmap(topGestures[i].bitmap) // set image
                    tv7.text = "2. " + topGestures[i].name
                }
                else if (i == 2) {
                    bitmapView8.setImageBitmap(topGestures[i].bitmap) // set image
                    tv8.text = "3. " + topGestures[i].name
                }
            }
        }

        clearButton.setOnClickListener() {
            canvasView.clear()
            canvasView.invalidate()
            bitmapView6.setImageBitmap(null) // remove image
            bitmapView7.setImageBitmap(null)
            bitmapView8.setImageBitmap(null)
            tv6.text = ""
            tv7.text = ""
            tv8.text = ""
        }

        return root
    }
}