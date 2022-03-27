package com.yenmin.ordermeal

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.gson.Gson

class OrderFragment(num: String) :Fragment(){
    private lateinit var database: FirebaseDatabase
    private lateinit var mealRef: DatabaseReference
    private lateinit var sideDishRef: DatabaseReference
    private lateinit var tempOrderRef: DatabaseReference
    var num = num
    var space = 50
    lateinit var meal: String
    lateinit var sideDish: ArrayList<String>
    //接著宣告兩個arraylist，一個用來裝meal的名字，另一個用來裝sideDish的名字(裝之前先把從OrderFragment傳過來的list先flatten再裝)
    //lateinit var mealList:ArrayList<String>
    //lateinit var sideDishList:ArrayList<String>
    var mealMap = mutableMapOf<String,Int>("牛排" to 0,"豬排" to 0,"魚排" to 0)
    var mealIdxMap = mutableMapOf<String,Int>("牛排" to 0,"豬排" to 0,"魚排" to 0)
    var sideDishMap = mutableMapOf<String,Int>("沙拉" to 0,"玉米濃湯" to 0,"馬鈴薯" to 0,"義大利麵" to 0)
    var sideDishIdxMap = mutableMapOf<String,Int>("沙拉" to 0,"玉米濃湯" to 0,"馬鈴薯" to 0,"義大利麵" to 0)
    private lateinit var mealadapter: MealRecyclerAdapter
    private lateinit var sidedishadapter: SideDishRecyclerAdapter
    private var orderMeal = ArrayList<Order>()
    private var orderSideDish = ArrayList<Order>()
    private var order = ArrayList<Order>()
    lateinit var decoration:RecyclerViewItemSpace
    private lateinit var btn_sendOrder:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        decoration = RecyclerViewItemSpace()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Log.e("CartFragment","onCreateView")

        return inflater.inflate(R.layout.fragment_order,container,false)
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.e("CartFragment","onViewCreated")
        var tv_meal = getView()?.findViewById<TextView>(R.id.tv_meal)
        var tv_sideDish = getView()?.findViewById<TextView>(R.id.tv_sideDish)
        val rv_meal = getView()?.findViewById<RecyclerView>(R.id.rv_meal)
        val rv_sideDish = getView()?.findViewById<RecyclerView>(R.id.rv_sideDish)
        btn_sendOrder = getView()?.findViewById<Button>(R.id.btn_sendOrder)!!
        var tv_num = getView()?.findViewById<TextView>(R.id.tv_num)

        database = FirebaseDatabase.getInstance()
        mealRef = database.getReference("meal")
        sideDishRef = database.getReference("sideDish")
        tempOrderRef = database.getReference("temp_order")
        FirebaseApp.initializeApp(requireActivity())
        /*if (rv_meal != null) {
            rv_meal.addItemDecoration(RecyclerViewItemSpace(space))
        }
        if (rv_sideDish != null) {
            rv_sideDish.addItemDecoration(RecyclerViewItemSpace(space))
        }*/
        //創建 MyRecyclerAdapter 並連結 recyclerView
        mealadapter = MealRecyclerAdapter(orderMeal,num)
        sidedishadapter = SideDishRecyclerAdapter(orderSideDish,num)

        if (rv_meal != null) {
            rv_meal.addItemDecoration(decoration)
            rv_meal.layoutManager = LinearLayoutManager(requireActivity())
            rv_meal.adapter = mealadapter
        }
        if (rv_sideDish != null) {
            rv_sideDish.addItemDecoration(decoration)
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
        tempOrderRef.child(num).child("meal").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (orderMeal.size < dataSnapshot.children.count()){
                            for (i in dataSnapshot.children){
                                orderMeal.add(Order(i.key.toString(), Integer.parseInt(i.value.toString())))
                            }
                        }else{
                            for (i in dataSnapshot.children){
                                for (j in orderMeal){
                                    if (i.key == j.name){
                                        j.number = Integer.parseInt(i.value.toString())
                                        break
                                    }else{
                                        continue
                                        //orderMeal.add(Order(i.key.toString(), Integer.parseInt(i.value.toString())))
                                    }
                                    orderMeal.add(Order(i.key.toString(), Integer.parseInt(i.value.toString())))
                                    break
                                }
                            }
                        }

                        mealadapter.notifyDataSetChanged()
                    }
                }
            }
        )


        tempOrderRef.child(num).child("sideDish").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (orderSideDish.size < dataSnapshot.children.count()){
                        for (i in dataSnapshot.children){
                            orderSideDish.add(Order(i.key.toString(), Integer.parseInt(i.value.toString())))
                        }
                    }else{
                        for (i in dataSnapshot.children){
                            for (j in orderSideDish){
                                if (i.key == j.name){
                                    j.number = Integer.parseInt(i.value.toString())
                                    break
                                }else{
                                    continue
                                    //orderMeal.add(Order(i.key.toString(), Integer.parseInt(i.value.toString())))
                                }
                                orderSideDish.add(Order(i.key.toString(), Integer.parseInt(i.value.toString())))
                                break
                            }
                        }
                    }

                    sidedishadapter.notifyDataSetChanged()
                }
            }
        }
        )

        btn_sendOrder.setOnClickListener {

            val childUpdates = hashMapOf<String, Any>(
                "order" to "已送出"
            )
            database.getReference("temp_order").child(num).updateChildren(childUpdates).addOnSuccessListener {
                btn_sendOrder.isEnabled = false
                Toast.makeText(requireActivity(),"訂單送出成功",Toast.LENGTH_SHORT)
            }.addOnFailureListener {
                Toast.makeText(requireActivity(),"訂單送出失敗",Toast.LENGTH_SHORT)
            }


        }




        /*fragmentManager?.setFragmentResultListener("toCart",viewLifecycleOwner){ key,bundle ->
            meal = bundle.getString("meal").toString()
            sideDish = bundle.getStringArrayList("sideDish") as ArrayList<String>
            if(meal in mealMap.keys){
                Log.e("meal in mealMap","true")
                var mealNum = mealMap[meal]?.plus(1)

                if (mealNum != null) {
                    mealMap[meal] = mealNum.toInt()
                }
                if (mealNum != null) {
                    if(mealNum > 1){
                        orderMeal[mealIdxMap[meal]!!].number = mealNum
                    }else{
                        orderMeal.add(Order(meal, mealMap.get(meal)!!))
                        mealIdxMap[meal] = orderMeal.size-1
                    }
                }

                mealadapter.notifyDataSetChanged()
            }
            if(sideDish != null){
                for(i in sideDish){
                    if(i in sideDishMap.keys){
                        var sideDishNum = sideDishMap[i]?.plus(1)

                        if (sideDishNum != null) {
                            sideDishMap[i] = sideDishNum.toInt()
                        }
                        if (sideDishNum != null) {
                            Log.e("$i",orderSideDish.toString())
                            Log.e("$i",sideDishIdxMap.toString())
                            if(sideDishNum > 1){
                                orderSideDish[sideDishIdxMap[i]!!].number = sideDishNum
                            }else{
                                orderSideDish.add(Order(i, sideDishMap.get(i)!!))
                                sideDishIdxMap[i] = orderSideDish.size-1
                            }
                        }
                        sidedishadapter.notifyDataSetChanged()
                    }
                }
            }


            Log.e("meal",meal)
            Log.e("sideDish",sideDish.toString())
            Log.e("orderMealListSize", orderMeal.size.toString())
            Log.e("sideDishListSize", orderSideDish.size.toString())
            Log.e("已執行到加到購物車","true")
        }*/


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Log.e("CartFragment","onActivityCreated")
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
        //Log.e("CartFragment","onStart")
    }
}
data class Order(
    val name: String,
    var number: Int
)