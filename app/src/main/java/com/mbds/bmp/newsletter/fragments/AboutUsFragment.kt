package com.mbds.bmp.newsletter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mbds.bmp.newsletter.MainActivity
import com.mbds.bmp.newsletter.R

class AboutUsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.setTitle(R.string.about_us)
        val activity = activity as MainActivity
        activity.setMenuVisible(false)
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onDestroy() {
        val activity = activity as MainActivity
        activity.setMenuVisible(true)
        super.onDestroy()
    }
}