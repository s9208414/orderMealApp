package com.yenmin.ordermeal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,num: String): FragmentPagerAdapter(fragmentManager) {
    private val FragmentTitleList: List<String> = listOf("菜單","購物車")
    val num = num

    /*override fun getItemCount(): Int {
        return FragmentTitleList.size
    }

    override fun createFragment(position: Int): Fragment {

        when(position){
            0 -> return OrderFragment(num)
            1 -> return CartFragment(num)
            else -> return OrderFragment(num)
        }
    }*/

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Fragment {
        TODO("Not yet implemented")
    }
}