package com.yenmin.ordermeal

import android.content.Context
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.gson.Gson

class MenuFragment(num: String):Fragment(){
    private lateinit var database: FirebaseDatabase
    private lateinit var mealRef: DatabaseReference
    private lateinit var sideDishRef: DatabaseReference
    private lateinit var tempOrderRef: DatabaseReference
    var num = num
    //var fg2 = fg2
    //var map_meal = mutableMapOf<String,String>()
    lateinit var meal:String
    //var map_sideDish = mutableMapOf<String,List<String>>()
    var sideDish = mutableListOf<String>()
    lateinit var salad:String
    lateinit var cornSoup:String
    lateinit var potato:String
    lateinit var spaghetti:String
    var isadded:Boolean = false
    //val cb_Id = arrayListOf(R.id.cb_salad,R.id.cb_cornSoup,R.id.cb_potato,R.id.cb_spaghetti)
    //var cb_sideDish = arrayListOf()
    var mealList = mutableListOf<Meal>()
    var supplyMealList = mutableListOf<Boolean>()
    var sideDishList = mutableListOf<SideDish>()
    var supplySideDishList = mutableListOf<Boolean>()
    var radioButtonIdList = mutableListOf<Int>()
    var radioButtonList = mutableListOf<RadioButton>()
    var checkBoxList = mutableListOf<CheckBox>()
    lateinit var tv_sideDish: TextView
    lateinit var btn_add2Cart: Button
    var initMeal = 0
    var initSideDish = 0
    lateinit var rg:RadioGroup
    var initTo0 = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("MenuFragment","onViewCreated")
        tv_sideDish = requireView().findViewById(R.id.tv_sideDish)
        btn_add2Cart = requireView().findViewById(R.id.btn_add2Cart)
        rg = requireView().findViewById(R.id.radioGroup)
        database = FirebaseDatabase.getInstance()
        mealRef = database.getReference("meal")
        sideDishRef = database.getReference("sideDish")
        tempOrderRef = database.getReference("temp_order")
        FirebaseApp.initializeApp(requireActivity())


        val childUpdates = hashMapOf<String, Any>(
            "id" to 0
        )
        tempOrderRef.child(num).updateChildren(childUpdates)

        tempOrderRef.child(num).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (i in dataSnapshot.children){
                        if (i.key == "status"){
                            if (i.value.toString() == "空桌"){
                                initTo0 = true
                            }
                        }
                        Log.e("i.value",i.value.toString())
                    }
                }
            }
        })


        mealRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (i in dataSnapshot.children){
                        //在這裡依序動態建立RadioButton
                        val radioButton = RadioButton(requireActivity())
                        var mealFromBase = Gson().fromJson(i.value.toString(),Meal::class.java)
                        mealList.add(mealFromBase)
                        supplyMealList.add(mealFromBase.supply)
                        if (initTo0 == true){
                            val childUpdates = hashMapOf<String, Any>(
                                "${mealFromBase.name}" to 0
                            )
                            tempOrderRef.child(num).child("meal").updateChildren(childUpdates)
                        }


                        radioButton.id = str2int("cb_meal_${i.key}")
                        radioButtonIdList.add(radioButton.id)
                        radioButton.text = mealFromBase.name
                        radioButton.layoutParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT)
                        for (i in mealList){
                            if(i.supply == true){
                                radioButton.text = mealFromBase.name
                                radioButton.isEnabled = true
                                radioButton.toggle()
                            }else{
                                radioButton.text = mealFromBase.name + " (售罄)"
                                radioButton.isEnabled = false
                                radioButton.toggle()
                            }
                        }
                        radioButton.setOnCheckedChangeListener { buttonView, ischecked ->
                            if (ischecked == true){
                                meal = mealFromBase.name
                                Log.e("meal",meal)
                            }else if(ischecked == false){
                                //Log.e("$meal","未勾選")
                            }
                        }
                        if (rg != null) {
                            rg.addView(radioButton)
                        }
                        radioButtonList.add(radioButton)



                    }
                    initMealState()
                    Log.e("meal",meal)
                }
            }
        })

        sideDishRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            var recordLastCheckBoxId = 0
            val fragment_order = getView()?.findViewById<ConstraintLayout>(R.id.fragment_order)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (i in dataSnapshot.children){
                        //在這裡依序動態建立CheckBox
                        val checkBox = CheckBox(requireActivity())
                        var sideDishFromBase = Gson().fromJson(i.value.toString(),SideDish::class.java)
                        sideDishList.add(sideDishFromBase)
                        supplySideDishList.add(sideDishFromBase.supply)

                        if (initTo0 == true){
                            val childUpdates = hashMapOf<String, Any>(
                                "${sideDishFromBase.name}" to 0
                            )
                            tempOrderRef.child(num).child("sideDish").updateChildren(childUpdates)
                        }


                        checkBox.id = str2int("cb_sideDish_${i.key}")

                        checkBox.text = sideDishFromBase.name
                        Log.e("checkBox.id",checkBox.id.toString())

                        checkBox.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT)
                        //checkBox.background = R.drawable.ic_baseline_clear_24
                        val checkBox_params = checkBox.layoutParams as? ConstraintLayout.LayoutParams
                        if (checkBox_params != null) {
                            Log.e("checkBox_params",checkBox_params.toString())
                            checkBox_params.setMargins(30)
                        }

                        if(i.key == "1"){
                            if (checkBox_params != null) {
                                checkBox_params.startToStart = tv_sideDish.id
                                checkBox_params.topToBottom = tv_sideDish.id
                            }
                        }else{
                            val temp  = i.key?.toInt()?.minus(1)
                            if (checkBox_params != null) {
                                //checkBox_params.topToBottom = getResources().getIdentifier("cb_meal_$temp", "id", activity?.getPackageName())
                                checkBox_params.topToBottom = recordLastCheckBoxId
                            }
                        }


                        for (i in sideDishList){
                            if(i.supply == true){
                                checkBox.text = sideDishFromBase.name
                                checkBox.isEnabled = true
                                checkBox.toggle()
                            }else{
                                checkBox.text = sideDishFromBase.name + " (售罄)"
                                checkBox.isEnabled = false
                                checkBox.toggle()
                            }
                        }

                        checkBox.setOnCheckedChangeListener { buttonView, ischecked ->
                            if(ischecked){
                                if(sideDishFromBase.name !in sideDish){
                                    sideDish.add(sideDishFromBase.name)
                                    //Toast.makeText(requireActivity(),"目前已選擇:$sideDish",Toast.LENGTH_SHORT).show()
                                    Log.e("sideDish",sideDish.toString())
                                }
                            }else if(!ischecked){
                                if(sideDishFromBase.name in sideDish){
                                    sideDish.remove(sideDishFromBase.name)
                                    //Toast.makeText(requireActivity(),"目前已選擇:$sideDish",Toast.LENGTH_SHORT).show()
                                    Log.e("sideDish",sideDish.toString())
                                }
                            }

                        }

                        checkBox.requestLayout()
                        if (fragment_order != null) {
                            fragment_order.addView(checkBox)
                        }
                        Log.e("id",checkBox.id.toString())
                        checkBoxList.add(checkBox)
                        recordLastCheckBoxId = checkBox.id
                    }
                    initSideDishState()
                    val btn_add2Cart_params = btn_add2Cart.layoutParams as ConstraintLayout.LayoutParams
                    btn_add2Cart_params.topToBottom = recordLastCheckBoxId
                    btn_add2Cart_params.startToStart = recordLastCheckBoxId
                    Log.e("recordLastCheckBoxId",recordLastCheckBoxId.toString())
                    btn_add2Cart.requestLayout()
                    Log.e("sideDish",sideDish.toString())
                }
            }
        })





        /*val cb_salad = getView()?.findViewById<CheckBox>(R.id.cb_salad)
        if (cb_salad != null) {
            this.salad = cb_salad.text.toString()
        }
        if (cb_salad != null) {
            cb_salad.setOnCheckedChangeListener{ buttonView, ischecked ->
                if(this.salad !in this.sideDish){
                    this.sideDish.add(this.salad)
                    Log.e("this.sideDish", this.sideDish.toString())
                    Log.e("this.salad", this.salad.toString())
                    //Toast.makeText(requireActivity(),"沙拉被點選",Toast.LENGTH_SHORT).show()
                    for(i in this.sideDish){
                        Log.e("i",i)
                    }
                    Toast.makeText(requireActivity(),"目前已選擇:$sideDish",Toast.LENGTH_SHORT).show()
                }else {
                    if(this.salad in this.sideDish){
                        this.sideDish.remove(this.salad)
                        for(i in this.sideDish){
                            Log.e("i",i)
                        }
                        Toast.makeText(requireActivity(),"目前已選擇:$sideDish",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val cb_cornSoup = getView()?.findViewById<CheckBox>(R.id.cb_cornSoup)
        if (cb_cornSoup != null) {
            this.cornSoup = cb_cornSoup.text.toString()
        }
        if (cb_cornSoup != null) {
            cb_cornSoup.setOnCheckedChangeListener{ buttonView, ischecked ->
                if(this.cornSoup !in this.sideDish){
                    this.sideDish.add(this.cornSoup)
                    //Toast.makeText(requireActivity(),"玉米濃湯被點選",Toast.LENGTH_SHORT).show()
                    for(i in this.sideDish){
                        Log.e("i",i)
                    }
                    Toast.makeText(requireActivity(),"目前已選擇:$sideDish",Toast.LENGTH_SHORT).show()
                }else {
                    if(this.cornSoup in this.sideDish){
                        this.sideDish.remove(this.cornSoup)
                        for(i in this.sideDish){
                            Log.e("i",i)
                        }
                        Toast.makeText(requireActivity(),"目前已選擇:$sideDish",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val cb_potato = getView()?.findViewById<CheckBox>(R.id.cb_potato)
        if (cb_potato != null) {
            this.potato = cb_potato.text.toString()
        }
        if (cb_potato != null) {
            cb_potato.setOnCheckedChangeListener{ buttonView, ischecked ->
                if(this.potato !in this.sideDish){
                    this.sideDish.add(this.potato)
                    //Toast.makeText(requireActivity(),"馬鈴薯被點選",Toast.LENGTH_SHORT).show()
                    for(i in this.sideDish){
                        Log.e("i",i)
                    }
                    Toast.makeText(requireActivity(),"目前已選擇:$sideDish",Toast.LENGTH_SHORT).show()
                }else {
                    if(this.potato in this.sideDish){
                        this.sideDish.remove(this.potato)
                        for(i in this.sideDish){
                            Log.e("i",i)
                        }
                        Toast.makeText(requireActivity(),"目前已選擇:$sideDish",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val cb_spaghetti = getView()?.findViewById<CheckBox>(R.id.cb_spaghetti)
        if (cb_spaghetti != null) {
            this.spaghetti = cb_spaghetti.text.toString()
        }
        if (cb_spaghetti != null) {
            cb_spaghetti.setOnCheckedChangeListener{ buttonView, ischecked ->
                if(this.spaghetti !in this.sideDish){
                    this.sideDish.add(this.spaghetti)
                    //Toast.makeText(requireActivity(),"義大利麵被點選",Toast.LENGTH_SHORT).show()
                    for(i in this.sideDish){
                        Log.e("i",i)
                    }
                    Toast.makeText(requireActivity(),"目前已選擇:$sideDish",Toast.LENGTH_SHORT).show()
                }else {
                    if(this.spaghetti in this.sideDish){
                        this.sideDish.remove(this.spaghetti)
                        for(i in this.sideDish){
                            Log.e("i",i)
                        }
                        Toast.makeText(requireActivity(),"目前已選擇:$sideDish",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }*/

        var tv_num = getView()?.findViewById<TextView>(R.id.tv_num)
        meal = ""



        /*intent?.extras?.let{
            this.num = it.getString("num").toString()
            Log.e("num",this.num)
            if (tv_num != null) {
                tv_num.text = "桌號:"+this.num
            }
        }*/


        if (tv_num != null) {
            //Log.e("num",name)
            tv_num.text = "桌號:"+" "+this.num
        }

        /*if (radioGroup != null) {
            radioGroup.setOnCheckedChangeListener{_,i->
                this.meal = when(i){
                    R.id.rb_beef -> "牛排"
                    R.id.rb_pork -> "豬排"
                    R.id.rb_fish -> "魚排"
                    else -> "未知"

                }
            }

        }*/


        if (btn_add2Cart != null) {
            btn_add2Cart.setOnClickListener{
                if(meal != "" && sideDish.isNotEmpty()){
                    isadded = true
                    var b = Bundle()
                    //b.putString("num",this.num)
                    b.putString("meal",this.meal)
                    tempOrderRef.child(num).child("meal").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {}
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (i in dataSnapshot.children){
                                    if (meal == i.key){
                                        var value = Integer.parseInt(i.value.toString())
                                        value+=1
                                        Log.e("value",value.toString())
                                        val childUpdates = hashMapOf<String, Any>(
                                            "${meal}" to value
                                        )
                                        tempOrderRef.child(num).child("meal").updateChildren(childUpdates)
                                    }

                                }

                            }
                        }

                    })


                    var sideDishList = ArrayList<String>()
                    this.sideDish.forEach{
                            item -> sideDishList.add(item)
                    }
                    b.putStringArrayList("sideDish",sideDishList)

                    tempOrderRef.child(num).child("sideDish").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {}
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (i in dataSnapshot.children){
                                    for (j in sideDish){
                                        if (j == i.key){
                                            var value = Integer.parseInt(i.value.toString())
                                            value+=1
                                            Log.e("value",value.toString())
                                            val childUpdates = hashMapOf<String, Any>(
                                                "$j" to value
                                            )
                                            tempOrderRef.child(num).child("sideDish").updateChildren(childUpdates)
                                        }
                                    }


                                }

                            }
                        }

                    })


                    // Use the Kotlin extension in the fragment-ktx artifact
                    fragmentManager?.setFragmentResult("toCart", b)
                    val mainActivity = activity as MainActivity
                    mainActivity.switch2Cart(isadded)

                }else if(meal == "" || sideDish.isEmpty()){
                    isadded = false
                    Toast.makeText(requireActivity(),"請選擇品項",Toast.LENGTH_SHORT).show()
                    //MainActivity().myViewPagerAdapter.getItem(1)
                }
                //val fm = requireActivity().supportFragmentManager
                //呼叫getFragmentManager()的方法來取得FragmentManager

                //呼叫getFragmentManager()的方法來取得FragmentManager
                //fm.beginTransaction().replace(R.id.viewPager, CartFragment(num)).commit()
                //sm.iAmMSG("changeFragment")

                //requireActivity().supportFragmentManager.beginTransaction().replace(R.id.viewPager,CartFragment(num)).commit()

            }
        }

    }
    fun initMealState(){
        if (initMeal == 0){

            rg.clearCheck()
            initMeal++
        }
    }
    fun initSideDishState(){
        if (initSideDish == 0){
            for (i in checkBoxList){
                i.isChecked = true
                Log.e("checkBoxElement",i.toString())
                i.toggle()
            }
            initSideDish++
        }
    }
    fun str2int(str: String): Int{
        val n = str.length
        var result = 0
        str.forEach { c -> result = result * 10 + (c.code - 48) }
        return result
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //sm = (activity as SendMessages?)!!
        Log.e("MenuFragment","onActivityCreated")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //sm = context as SendMessages
    }
}

private operator fun Any?.plus(i: Int) {

}

private operator fun Any?.minus(i: Int) {

}

