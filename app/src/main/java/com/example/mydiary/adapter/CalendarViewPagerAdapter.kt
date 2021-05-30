package com.example.mydiary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalendarViewPagerAdapter(
    val fragmentList: MutableList<Fragment>,
    fm: FragmentManager,
    lifecycle: Lifecycle
):FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment  = fragmentList[position]

    fun setFragmentAtPosition(index: Int,fragment: Fragment) {
        var result = fragmentList.find {
            it == fragment
        }
        if (result == null) {
            fragmentList.set(index, fragment)
        }
        notifyItemChanged(index)
    }
}