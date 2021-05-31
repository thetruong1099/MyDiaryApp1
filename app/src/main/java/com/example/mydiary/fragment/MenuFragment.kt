package com.example.mydiary.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mydiary.R
import kotlinx.android.synthetic.main.fragment_menu.*


class MenuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onStart() {
        super.onStart()

        val navControler = findNavController()
        btn_back.setOnClickListener { navControler.popBackStack() }

        layout_day_start.setOnClickListener {
            val dayStartOfWeekFragment = DayStartOfWeekFragment()
            dayStartOfWeekFragment.show(childFragmentManager, "TAG")
        }
    }
}