package com.yenmin.ordermeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private val FragmentTitleList: List<String> = listOf("菜單","訂單")
    lateinit var num : String
    var switch : Boolean = false
    lateinit var myViewPagerAdapter:MyViewPagerAdapter
    lateinit var pager:ViewPager
    lateinit var fg1:MenuFragment
    lateinit var fg2:OrderFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pager = findViewById<ViewPager>(R.id.viewPager)
        var tL = findViewById<TabLayout>(R.id.tabs)
        tL.setupWithViewPager(pager)


        intent?.extras?.let{
            num = it.getInt("number").toString()
            Log.e("tablenum",num)
            fg2 = OrderFragment(num)
            fg1 = MenuFragment(num)
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
class MyViewPagerAdapter(fm: FragmentManager, num : String, fg1: MenuFragment, fg2: OrderFragment) : FragmentStatePagerAdapter(fm){
    var num = num
    val fg1 = fg1
    val fg2 = fg2
    private val FragmentTitleList: List<String> = listOf("菜單","訂單")
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