package com.example.mydiary.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydiary.R
import com.example.mydiary.activity.DetailDiaryActivity
import com.example.mydiary.adapter.AllDiaryAdapter
import com.example.mydiary.model.Diary
import com.example.mydiary.viewmodel.DiaryViewModel
import kotlinx.android.synthetic.main.activity_write_diary.*
import kotlinx.android.synthetic.main.fragment_diary.*

class DiaryFragment : Fragment() {

    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelFactory(requireActivity().application)
        )[DiaryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary, container, false)
    }

    override fun onStart() {
        super.onStart()

        val allDiaryAdapter = AllDiaryAdapter(onItemClick)

        rv_diary.apply {
            adapter = allDiaryAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        diaryViewModel.getAllDiary().observe(this, Observer {
            if (it.size==0) tv_nofication_no_data.visibility = View.GONE
            else{
                it.sortWith(compareBy<Diary>({ it.year }, { it.month }, {it.date}, {it.time}).reversed())
                allDiaryAdapter.setListDiary(it)
            }
        })

        val navControler = findNavController()
        btn_back.setOnClickListener { navControler.popBackStack() }
        btn_search.setOnClickListener {
            var searchFragment = SearchFragment()
            searchFragment.show(childFragmentManager, "TAG")

        }
    }

    private val onItemClick:(diary:Diary)->Unit={
        var intent = Intent(context, DetailDiaryActivity::class.java)
        intent.putExtra("Detail Diary",it)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}