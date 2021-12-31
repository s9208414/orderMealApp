package com.yenmin.ordermeal

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

class CartFragment(num: String) :Fragment(){
    var num = num
    lateinit var meal: String
    lateinit var sideDish: ArrayList<String>
    //接著宣告兩個arraylist，一個用來裝meal的名字，另一個用來裝sideDish的名字(裝之前先把從OrderFragment傳過來的list先flatten再裝)
    //lateinit var mealList:ArrayList<String>
    //lateinit var sideDishList:ArrayList<String>
    var mealMap = mutableMapOf<String,Int>("牛排" to 0,"豬排" to 0,"魚排" to 0)
    var sideDishMap = mutableMapOf<String,Int>("沙拉" to 0,"玉米濃湯" to 0,"馬鈴薯" to 0,"義大利麵" to 0)
    private lateinit var mealadapter: MealRecyclerAdapter
    private lateinit var sidedishadapter: SideDishRecyclerAdapter
    private var orderMeal = ArrayList<Order>()
    private var orderSideDish = ArrayList<Order>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*try {
            meal = arguments?.get("meal") as String
            sideDish = arguments?.get("sideDish") as ArrayList<String>
            //Toast.makeText(requireActivity(),"已收到資料",Toast.LENGTH_SHORT)
            Log.e("資料已收到","true")
        }catch (E: Exception){
            meal = ""
            sideDish = ArrayList()
            sideDish.add("")
            //Toast.makeText(requireActivity(),"您尚未點餐",Toast.LENGTH_SHORT).show()
            Log.e("資料已收到","false")
        }

        if(meal in mealMap.keys){
            mealMap[meal] = 1
            order.add(Order(meal, mealMap.get(meal)!!))
        }
        if(sideDish != null){
            for(i in sideDish){
                if(i in sideDishMap.keys){
                    sideDishMap[i] = 1
                    order.add(Order(i, sideDishMap.get(i)!!))
                }
            }
        }*/

        Log.e("CartFragment","onCreateView")

        return inflater.inflate(R.layout.fragment_cart,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("CartFragment","onViewCreated")
        var tv_meal = getView()?.findViewById<TextView>(R.id.tv_meal)
        var tv_sideDish = getView()?.findViewById<TextView>(R.id.tv_sideDish)
        val rv_meal = getView()?.findViewById<RecyclerView>(R.id.rv_meal)
        val rv_sideDish = getView()?.findViewById<RecyclerView>(R.id.rv_sideDish)
        val btn_sendOrder = getView()?.findViewById<Button>(R.id.btn_sendOrder)
        var tv_num = getView()?.findViewById<TextView>(R.id.tv_num)
        //創建 MyRecyclerAdapter 並連結 recyclerView
        mealadapter = MealRecyclerAdapter(orderMeal)
        sidedishadapter = SideDishRecyclerAdapter(orderSideDish)
        if (rv_meal != null) {
            rv_meal.layoutManager = LinearLayoutManager(requireActivity())
            rv_meal.adapter = mealadapter
        }
        if (rv_sideDish != null) {
            rv_sideDish.layoutManager = LinearLayoutManager(requireActivity())
            rv_sideDish.adapter = sidedishadapter
        }


        /*intent?.extras?.let{
            this.num = it.getString("num").toString()
            Log.e("num",this.num)
            tv_num.text = "桌號:"+this.num
        }*/

        val bundle = requireActivity().intent.extras
        val name = bundle!!.getString("num").toString()
        if (tv_num != null) {
            tv_num.text = "桌號:"+" "+this.num
        }
        fragmentManager?.setFragmentResultListener("toCart",viewLifecycleOwner){ key,bundle ->
            meal = bundle.getString("meal").toString()
            sideDish = bundle.getStringArrayList("sideDish") as ArrayList<String>
            if(meal in mealMap.keys){
                Log.e("meal in mealMap","true")
                mealMap[meal] = 1
                orderMeal.add(Order(meal, mealMap.get(meal)!!))
                mealadapter.notifyDataSetChanged()
            }
            if(sideDish != null){
                for(i in sideDish){
                    if(i in sideDishMap.keys){
                        sideDishMap[i] = 1
                        orderSideDish.add(Order(i, sideDishMap.get(i)!!))
                        sidedishadapter.notifyDataSetChanged()
                    }
                }
            }


            Log.e("meal",meal)
            Log.e("sideDish",sideDish.toString())
            Log.e("orderMealListSize", orderMeal.size.toString())
            Log.e("sideDishListSize", orderSideDish.size.toString())
            Log.e("已執行到加到購物車","true")
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("CartFragment","onActivityCreated")
        /*try {
            meal = arguments?.get("meal") as String
            sideDish = arguments?.get("sideDish") as ArrayList<String>
        }catch (E: Exception){
            meal = ""
            sideDish = ArrayList()
            sideDish.add("")
            Toast.makeText(requireActivity(),"您尚未點餐",Toast.LENGTH_SHORT).show()
        }

        if(meal in mealMap.keys){
            mealMap[meal] = 1
            order.add(Order(meal, mealMap.get(meal)!!))
        }
        if(sideDish != null){
            for(i in sideDish){
                if(i in sideDishMap.keys){
                    sideDishMap[i] = 1
                    order.add(Order(i, sideDishMap.get(i)!!))
                }
            }
        }*/
    }

    /*fun check(){
        try {
            meal = arguments?.get("meal") as String
            sideDish = arguments?.get("sideDish") as ArrayList<String>
            Log.e("check","true")
        }catch (E: Exception){
            meal = ""
            sideDish = ArrayList()
            sideDish.add("")
            Toast.makeText(requireActivity(),"您尚未點餐",Toast.LENGTH_SHORT).show()
        }
    }*/


    override fun onStart() {
        super.onStart()
        Log.e("CartFragment","onStart")
    }
}
data class Order(
    val name: String,
    val number: Int
)