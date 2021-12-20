package com.yenmin.ordermeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.os.bundleOf

class MainActivity : AppCompatActivity() {
    var num:String = ""
    //var map_meal = mutableMapOf<String,String>()
    lateinit var meal:String
    //var map_sideDish = mutableMapOf<String,List<String>>()
    var sideDish = mutableListOf<String>()
    lateinit var salad:String
    lateinit var cornSoup:String
    lateinit var potato:String
    lateinit var spaghetti:String
    //var cart = mutableMapOf<String,Pair<Map<String,String>,Map<String,List<String>>>>()
    //var cart = mutableMapOf<String,Map<String,Set<String>>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_add2Cart = findViewById<Button>(R.id.btn_add2Cart)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val cb_salad = findViewById<CheckBox>(R.id.cb_salad)
        this.salad = cb_salad.toString()
        val cb_cornSoup = findViewById<CheckBox>(R.id.cb_cornSoup)
        this.cornSoup = cb_cornSoup.toString()
        val cb_potato = findViewById<CheckBox>(R.id.cb_potato)
        this.potato = cb_potato.toString()
        val cb_spaghetti = findViewById<CheckBox>(R.id.cb_spaghetti)
        this.spaghetti = cb_spaghetti.toString()
        var tv_num = findViewById<TextView>(R.id.tv_num)


        intent?.extras?.let{
            this.num = it.getString("num").toString()
            Log.e("num",this.num)
            tv_num.text = "桌號:"+this.num
        }
        radioGroup.setOnCheckedChangeListener{_,i->
            this.meal = when(i){
                R.id.rb_beef -> "牛排"
                R.id.rb_pork -> "豬排"
                R.id.rb_fish -> "魚排"
                else -> "未知"
            }
        }


        btn_add2Cart.setOnClickListener{
            var b = Bundle()
            b.putString("num",this.num)
            b.putString("meal",this.meal)
            var sideDishList = ArrayList<String>()
            this.sideDish.forEach{
                item -> sideDishList.add(item)
            }
            b.putStringArrayList("sideDish",sideDishList)

            //val pair = Pair(this.map_meal,this.map_sideDish)
            //cart[this.num,Pair(this.map_meal,this.map_sideDish)]
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
}