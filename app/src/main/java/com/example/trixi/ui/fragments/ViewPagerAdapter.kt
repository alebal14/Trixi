package com.example.trixi.ui.fragments

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager){

    val fragmentList: MutableList<Fragment> = ArrayList()
    val fragmentTitleList: MutableList<String> = ArrayList()


    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }


    @Nullable
    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList[position]
    }


    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }
}


