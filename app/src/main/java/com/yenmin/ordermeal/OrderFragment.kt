package com.yenmin.ordermeal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class OrderFragment(num: String):Fragment(){
    var num = num
    //var map_meal = mutableMapOf<String,String>()
    lateinit var meal:String
    //var map_sideDish = mutableMapOf<String,List<String>>()
    var sideDish = mutableListOf<String>()
    lateinit var salad:String
    lateinit var cornSoup:String
    lateinit var potato:String
    lateinit var spaghetti:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn_add2Cart = getView()?.findViewById<Button>(R.id.btn_add2Cart)
        val radioGroup = getView()?.findViewById<RadioGroup>(R.id.radioGroup)
        val cb_salad = getView()?.findViewById<CheckBox>(R.id.cb_salad)
        this.salad = cb_salad.toString()
        val cb_cornSoup = getView()?.findViewById<CheckBox>(R.id.cb_cornSoup)
        this.cornSoup = cb_cornSoup.toString()
        val cb_potato = getView()?.findViewById<CheckBox>(R.id.cb_potato)
        this.potato = cb_potato.toString()
        val cb_spaghetti = getView()?.findViewById<CheckBox>(R.id.cb_spaghetti)
        this.spaghetti = cb_spaghetti.toString()
        var tv_num = getView()?.findViewById<TextView>(R.id.tv_num)


        /*intent?.extras?.let{
            this.num = it.getString("num").toString()
            Log.e("num",this.num)
            if (tv_num != null) {
                tv_num.text = "桌號:"+this.num
            }
        }*/


        val name = num
        if (tv_num != null) {
            Log.e("num",name)
            print("num is $name")
            tv_num.text = "桌號:"+" "+this.num
        }

        if (radioGroup != null) {
            radioGroup.setOnCheckedChangeListener{_,i->
                this.meal = when(i){
                    R.id.rb_beef -> "牛排"
                    R.id.rb_pork -> "豬排"
                    R.id.rb_fish -> "魚排"
                    else -> "未知"
                }
            }
        }


        if (btn_add2Cart != null) {
            btn_add2Cart.setOnClickListener{
                var b = Bundle()
                b.putString("num",this.num)
                b.putString("meal",this.meal)
                var sideDishList = ArrayList<String>()
                this.sideDish.forEach{
                        item -> sideDishList.add(item)
                }
                b.putStringArrayList("sideDish",sideDishList)
                val fragment = CartFragment(num)
                fragment.arguments = b
                fragmentManager?.beginTransaction()?.replace(R.id.viewPager,fragment)?.commit()

                //val pair = Pair(this.map_meal,this.map_sideDish)
                //cart[this.num,Pair(this.map_meal,this.map_sideDish)]

            }
        }
    }
    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            var checked: Boolean = view.isChecked

            when (view.id) {
                R.id.cb_salad -> {
                    if(checked) {
                        if(this.salad !in this.sideDish){
                            this.sideDish.add(this.salad)
                        }
                    }else {
                        if(this.salad in this.sideDish){
                            this.sideDish.remove(this.salad)
                        }
                    }
                }
                R.id.cb_potato-> {
                    if(checked) {
                        if(this.potato !in this.sideDish){
                            this.sideDish.add(this.potato)
                        }
                    }else {
                        if(this.potato in this.sideDish){
                            this.sideDish.remove(this.potato)
                        }
                    }
                }
                R.id.cb_cornSoup-> {
                    if(checked) {
                        if(this.cornSoup !in this.sideDish){
                            this.sideDish.add(this.cornSoup)
                        }
                    }else {
                        if(this.cornSoup in this.sideDish){
                            this.sideDish.remove(this.cornSoup)
                        }
                    }
                }
                R.id.cb_spaghetti-> {
                    if(checked) {
                        if(this.spaghetti !in this.sideDish){
                            this.sideDish.add(this.spaghetti)
                        }
                    }else {
                        if(this.spaghetti in this.sideDish){
                            this.sideDish.remove(this.spaghetti)
                        }
                    }
                }
                // TODO: Add sideDish
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}