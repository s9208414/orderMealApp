package com.yenmin.ordermeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val FragmentTitleList: List<String> = listOf("菜單","購物車")
    lateinit var num : String
    var switch : Boolean = false
    lateinit var myViewPagerAdapter:MyViewPagerAdapter
    lateinit var pager:ViewPager
    lateinit var fg1:OrderFragment
    lateinit var fg2:CartFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pager = findViewById<ViewPager>(R.id.viewPager)
        var tL = findViewById<TabLayout>(R.id.tabs)
        tL.setupWithViewPager(pager)


        intent?.extras?.let{
            num = it.getString("num").toString()
            fg2 = CartFragment(num)
            fg1 = OrderFragment(num,fg2)
            /*pager.adapter = ViewPagerAdapter(supportFragmentManager,lifecycle,num)
            supportFragmentManager.beginTransaction().commit()*/
            myViewPagerAdapter = MyViewPagerAdapter(supportFragmentManager,num,fg1,fg2)
            pager.adapter = myViewPagerAdapter
            //Log.e("myViewPagerAdapter",myViewPagerAdapter.toString())

        }
        /*TabLayoutMediator(tL,pager){
                tab,position -> tab.text = FragmentTitleList[position]
        }.attach()*/

    }

    fun switch2Cart(isadded: Boolean){
        Log.e("switch","true")
        /*val cartfragment:CartFragment = myViewPagerAdapter.instantiateItem(pager,1) as CartFragment
        cartfragment.check()*/
        if(isadded == true){
            pager.setCurrentItem(1,true)
        }


        //supportFragmentManager.beginTransaction().replace(R.id.viewPager,fg2).commitNow()
        //myViewPagerAdapter.finishUpdate(pager)
        //switch = true
        //myViewPagerAdapter.getItem(1)
        //myViewPagerAdapter.setPrimaryItem()

    }



}
class MyViewPagerAdapter(fm: FragmentManager,num : String,fg1: OrderFragment,fg2: CartFragment) : FragmentStatePagerAdapter(fm){
    var num = num
    val fg1 = fg1
    val fg2 = fg2
    private val FragmentTitleList: List<String> = listOf("菜單","購物車")
    override fun getCount() = 2

    override fun getItem(position: Int) = when(position){
        0 -> fg1
        1 -> fg2
        else -> fg1

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return FragmentTitleList[position]
    }

}