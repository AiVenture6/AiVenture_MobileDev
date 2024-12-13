package com.example.aiventure.category

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CategoryPagerAdapter(
    activity: FragmentActivity,
    private val loadFragment: (Int) -> Fragment
) :
    FragmentStateAdapter(activity) {

    private val fragmentList = mutableMapOf<Int, Fragment>()

    private val tabCount = 3

    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        val fragment = loadFragment(position)
        fragmentList[position] = fragment
        return fragment
    }

    fun getFragment(position: Int): Fragment? {
        return fragmentList[position]
    }
}