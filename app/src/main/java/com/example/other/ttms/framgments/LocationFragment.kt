package com.example.other.ttms.framgments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.other.ttms.R

class LocationFragment: Fragment() {

    private lateinit var rootView:View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView=inflater.inflate(R.layout.popwin_area, null)
        return rootView
    }
}