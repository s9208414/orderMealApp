package com.yenmin.ordermeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val FragmentTitleList: List<String> = listOf("菜單","購物車")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var pager = findViewById<ViewPager2>(R.id.viewPager)
        var tL = findViewById<TabLayout>(R.id.tabs)
        pager.adapter = ViewPagerAdapter(supportFragmentManager,lifecycle)
        TabLayoutMediator(tL,pager){
                tab,position -> tab.text = FragmentTitleList[position]
        }.attach()

    }



}