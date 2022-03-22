package com.yenmin.ordermeal

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class ManagerActivity : AppCompatActivity()  {
    private val FragmentTitleList: List<String> = listOf("訂單接收","菜單編輯")
    lateinit var num : String
    lateinit var position : String
    var switch : Boolean = false
    lateinit var managerViewPagerAdapter:ManagerViewPagerAdapter
    lateinit var pager_manager: ViewPager
    lateinit var fg1:RecieveFragment
    lateinit var fg2:ModifyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        pager_manager = findViewById<ViewPager>(R.id.viewPager_manager)
        var tL_manager = findViewById<TabLayout>(R.id.tabs_manager)
        tL_manager.setupWithViewPager(pager_manager)

        /*var b = this.intent.extras

        num = b?.getString("id").toString()
        position = b?.getString("position").toString()
        Log.e("MA",num.toString())
        fg2 = ModifyFragment(num,position)
        fg1 = RecieveFragment(num,position)
        managerViewPagerAdapter = ManagerViewPagerAdapter(supportFragmentManager,num,fg1,fg2)
        pager_manager.adapter = managerViewPagerAdapter*/

        intent?.extras?.let{
            num = it.getString("id").toString()
            position = it.getString("position").toString()
            Log.e("MA",num.toString())
            fg2 = ModifyFragment(num,position)
            fg1 = RecieveFragment(num,position)
            /*pager.adapter = ViewPagerAdapter(supportFragmentManager,lifecycle,num)
            supportFragmentManager.beginTransaction().commit()*/
            managerViewPagerAdapter = ManagerViewPagerAdapter(supportFragmentManager,num,fg1,fg2)
            pager_manager.adapter = managerViewPagerAdapter
            //Log.e("myViewPagerAdapter",myViewPagerAdapter.toString())

        }

    }

    /*fun switch2Cart(isadded: Boolean){
        Log.e("switch","true")
        if(isadded == true){
            pager.setCurrentItem(1,true)
        }


    }*/



}
class ManagerViewPagerAdapter(fm: FragmentManager, num : String, fg1: RecieveFragment, fg2: ModifyFragment) : FragmentStatePagerAdapter(fm){
    var num = num
    val fg1 = fg1
    val fg2 = fg2
    private val FragmentTitleList: List<String> = listOf("訂單接收","菜單編輯")
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