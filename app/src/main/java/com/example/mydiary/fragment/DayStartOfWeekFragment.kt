package com.example.mydiary.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.R
import com.example.mydiary.viewmodel.SharedPreferenceViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_day_start_of_week.*


class DayStartOfWeekFragment : BottomSheetDialogFragment() {

    private val sharePreferenceViewModel by lazy {
        ViewModelProvider(
            this,
            SharedPreferenceViewModel.SharePreferenceViewModelFactory(requireContext())
        )[SharedPreferenceViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_start_of_week, container, false)
    }

    override fun onStart() {
        super.onStart()

        tv_mon.setOnClickListener {
            sharePreferenceViewModel.setDayStartOfWeek(tv_mon.text.toString())
            dismiss()
        }
        tv_tue.setOnClickListener {
            sharePreferenceViewModel.setDayStartOfWeek(tv_tue.text.toString())
            dismiss()
        }
        tv_wed.setOnClickListener {
            sharePreferenceViewModel.setDayStartOfWeek(tv_wed.text.toString())
            dismiss()
        }
        tv_thur.setOnClickListener {
            sharePreferenceViewModel.setDayStartOfWeek(tv_thur.text.toString())
            dismiss()
        }
        tv_fri.setOnClickListener {
            sharePreferenceViewModel.setDayStartOfWeek(tv_fri.text.toString())
            dismiss()
        }
        tv_sat.setOnClickListener {
            sharePreferenceViewModel.setDayStartOfWeek(tv_sat.text.toString())
            dismiss()
        }
        tv_sun.setOnClickListener {
            sharePreferenceViewModel.setDayStartOfWeek(tv_sun.text.toString())
            dismiss()
        }
    }
}