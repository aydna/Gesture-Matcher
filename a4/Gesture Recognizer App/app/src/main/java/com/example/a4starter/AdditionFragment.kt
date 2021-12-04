package com.example.a4starter

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Path
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class AdditionFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root: View = inflater.inflate(R.layout.fragment_addition, container, false)

        val canvasView: CanvasView = root.findViewById(R.id.canvasView)

        //mViewModel!!.desc.observe(viewLifecycleOwner, { s:String -> textView.text = "$s - Addition" }) // on desc change, update
        //mViewModel!!.strokeGestures.observe(viewLifecycleOwner, { s:ArrayList<Path> -> textView.text = "stroke count: ${s.size}"})

        val addButton: Button = root.findViewById(R.id.button2)
        addButton.setOnClickListener {

            // some snippets of this function used from CS349 discussion lecture by Jeff Avery
            val pathBitmap: Bitmap? = canvasView.drawToBitmap()
            //val imageBitmap = ImageView.setImageBitmap(pathBitmap)
            val currPath: Path? = canvasView.currPath
            val alert: AlertDialog.Builder = AlertDialog.Builder(canvasView.context)
            alert.setTitle("Name Gesture")

            val input = EditText(canvasView.context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            alert.setView(input)
            alert.setPositiveButton("Ok") { _, _ ->
                when {
                    currPath == null -> {
                        // currPath is null -> display alert
                        val nullAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                        nullAlertBuilder.setTitle("Invalid Operation")
                        nullAlertBuilder.setMessage("There is no drawn gesture")

                        val nullAlert: AlertDialog = nullAlertBuilder.create()
                        nullAlert.show()
                    }
                    input.text.toString() == "" -> {
                        // invalid. nothing entered
                        val noTextBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                        noTextBuilder.setTitle("Invalid Operation")
                        noTextBuilder.setMessage("Please enter name for gesture")

                        val nullAlert: AlertDialog = noTextBuilder.create()
                        nullAlert.show()
                    }
                    mViewModel!!.nameExists(input.text.toString()) -> {
                        // can't use this name
                        val noTextBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                        noTextBuilder.setTitle("Invalid Operation")
                        noTextBuilder.setMessage("Please enter unused name for gesture")

                        val nullAlert: AlertDialog = noTextBuilder.create()
                        nullAlert.show()
                    }
                    currPath != null -> {
                        if (pathBitmap != null) {
                            mViewModel!!.addStroke(currPath, pathBitmap, input.text.toString())
                        }
                        canvasView.clear() // clear canvas
                    }
                }
            }
            alert.setNegativeButton("Cancel") { _, _ ->
                // back out
            }

            val dr: Drawable = BitmapDrawable(pathBitmap)
            alert.setIcon(dr)
            val myAlert: AlertDialog = alert.create()
            myAlert.show()
        }

        val clearButton: Button = root.findViewById(R.id.button)
        clearButton.setOnClickListener() {
            // just clear canvas
            canvasView.clear()
        }


        return root
    }
}
