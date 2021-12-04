package com.example.a4starter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData

class GestureAdapter(context: Context?, gestures: ArrayList<SharedViewModel.gestureInfo>): ArrayAdapter<SharedViewModel.gestureInfo>(context!!, 0, gestures!!) {

    val gestures = gestures
    //val mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val gesture = getItem(position)

        view = LayoutInflater.from(context).inflate(R.layout.gesture_adapter, parent, false)

        // Add image for bitmapview
        //val bitmapView: BitmapView = view.findViewById(R.id.bitmapView)
        var imgView: ImageView = view.findViewById(R.id.imageView)

        if (gesture != null) {
            imgView.setImageBitmap(gesture.bitmap)
        }


        val textView: TextView = view.findViewById(R.id.textView)
        if (gesture != null) {
            textView.text = gesture.name
        }

        val delButton: Button = view.findViewById(R.id.button3)

        delButton.setOnClickListener(View.OnClickListener {
            gestures.removeAt(position)
            notifyDataSetChanged() // refresh view
        })

        return view
    }

}