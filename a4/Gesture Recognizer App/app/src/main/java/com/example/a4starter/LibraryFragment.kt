package com.example.a4starter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class LibraryFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_library, container, false)
        //val textView = root.findViewById<TextView>(R.id.text_library)

        //mViewModel!!.desc.observe(viewLifecycleOwner, { s:String -> textView.text = "$s - Library" })
        //mViewModel!!.strokeGestures.observe(viewLifecycleOwner, { s:ArrayList<Path> -> textView.text = "stroke count: ${s.size}"})

        val gestureAdapter: GestureAdapter = GestureAdapter(context, mViewModel!!.gesturesList)
        val listView: ListView = root.findViewById(R.id.listView1)
        listView.adapter = gestureAdapter

        return root
    }
}