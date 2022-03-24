package com.yenmin.ordermeal

import android.annotation.SuppressLint
import android.app.ActionBar
import android.os.Bundle
import android.text.TextUtils.indexOf
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.text.FieldPosition

class ModifyFragment(num_manger: String, position: String): Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var mealRef: DatabaseReference
    private lateinit var sideDishRef: DatabaseReference
    var num = num_manger
    var position = position
    lateinit var tv_meal:TextView
    lateinit var fragment_modify:ConstraintLayout
    lateinit var btn_modify: Button
    lateinit var tv_sideDish: TextView
    var mealList = mutableListOf<Meal>()
    var supplyMealList = mutableListOf<Boolean>()
    var sideDishList = mutableListOf<SideDish>()
    var supplySideDishList = mutableListOf<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_modify,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var tv_num_manager = getView()?.findViewById<TextView>(R.id.tv_num_manager)
        var tv_position = getView()?.findViewById<TextView>(R.id.tv_position)
        tv_meal = requireView().findViewById(R.id.tv_meal)
        fragment_modify = requireView().findViewById(R.id.fragment_modify)
        btn_modify = requireView().findViewById(R.id.btn_modify)
        tv_sideDish = requireView().findViewById(R.id.tv_sideDish)
        Log.e("number",num)
        //Log.e("tv_num_manager",tv_num_manager.toString())
        if (tv_num_manager != null) {
            tv_num_manager.text = "員工編號:"+" "+this.num
        }
        if (tv_position != null) {
            tv_position.text = "職位:"+" "+this.position
        }
        /*if (tv_num_manager != null) {
            Log.e("tv_num_manager.id",
                getResources().getIdentifier("tv_num_manager", "id", activity?.getPackageName()).toString()
            )
        }*/
        database = FirebaseDatabase.getInstance()
        mealRef = database.getReference("meal")
        sideDishRef = database.getReference("sideDish")
        FirebaseApp.initializeApp(requireActivity())

        mealRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            var recordLastCheckBoxId = 0
            val fragment_modify = getView()?.findViewById<ConstraintLayout>(R.id.fragment_modify)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (i in dataSnapshot.children){
                        //在這裡依序動態建立CheckBox
                        val checkBox = CheckBox(requireActivity())
                        var meal = Gson().fromJson(i.value.toString(),Meal::class.java)
                        mealList.add(meal)
                        supplyMealList.add(meal.supply)

                        checkBox.id = str2int("cb_meal_${i.key}")

                        checkBox.text = meal.name
                        Log.e("checkBox.id",checkBox.id.toString())

                        checkBox.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT)
                        //checkBox.background = R.drawable.ic_baseline_clear_24
                        val checkBox_params = checkBox.layoutParams as? ConstraintLayout.LayoutParams
                        if (checkBox_params != null) {
                            Log.e("checkBox_params",checkBox_params.toString())
                            checkBox_params.setMargins(30)
                        }
                        if (checkBox_params != null) {
                            checkBox_params.startToStart = tv_meal.id
                        }
                        if(i.key == "1"){
                            if (checkBox_params != null) {
                                checkBox_params.topToBottom = tv_meal.id
                            }
                        }else{
                            val temp  = i.key?.toInt()?.minus(1)
                            if (checkBox_params != null) {
                                //checkBox_params.topToBottom = getResources().getIdentifier("cb_meal_$temp", "id", activity?.getPackageName())
                                checkBox_params.topToBottom = recordLastCheckBoxId
                            }
                        }

                        val defaultDrawable = checkBox.buttonDrawable


                        for (i in mealList){
                            if(i.supply == true){
                                checkBox.text = meal.name
                                checkBox.isChecked = true
                                checkBox.setButtonDrawable(defaultDrawable)
                                checkBox.toggle()
                            }else{
                                checkBox.text = meal.name + " (售罄)"
                                checkBox.isChecked = false
                                checkBox.setButtonDrawable(R.drawable.ic_baseline_clear_24)
                                checkBox.toggle()
                            }
                        }

                        checkBox.setOnCheckedChangeListener { buttonView, ischecked ->
                            if (ischecked == true){
                                checkBox.text = meal.name + " (售罄)"
                                mealList[i.key?.toInt()!!-1].supply = false
                                //checkBox.buttonDrawable = ic_baseline_clear_24
                                checkBox.setButtonDrawable(R.drawable.ic_baseline_clear_24)
                            }else{
                                checkBox.text = meal.name
                                mealList[i.key?.toInt()!!-1].supply = true
                                checkBox.setButtonDrawable(defaultDrawable)
                            }

                        }

                        checkBox.requestLayout()
                        if (fragment_modify != null) {
                            fragment_modify.addView(checkBox)
                        }
                        Log.e("id",checkBox.id.toString())

                        recordLastCheckBoxId = checkBox.id
                    }
                    val tv_sideDish_params = tv_sideDish.layoutParams as ConstraintLayout.LayoutParams
                    tv_sideDish_params.topToBottom = recordLastCheckBoxId
                    Log.e("recordLastCheckBoxId",recordLastCheckBoxId.toString())
                    tv_sideDish.requestLayout()
                }
            }
        })

        sideDishRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            var recordLastCheckBoxId = 0
            val fragment_modify = getView()?.findViewById<ConstraintLayout>(R.id.fragment_modify)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (i in dataSnapshot.children){
                        //在這裡依序動態建立CheckBox
                        val checkBox = CheckBox(requireActivity())
                        var sideDish = Gson().fromJson(i.value.toString(),SideDish::class.java)
                        sideDishList.add(sideDish)
                        supplySideDishList.add(sideDish.supply)

                        checkBox.id = str2int("cb_sideDish_${i.key}")

                        checkBox.text = sideDish.name
                        Log.e("checkBox.id",checkBox.id.toString())

                        checkBox.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT)
                        //checkBox.background = R.drawable.ic_baseline_clear_24
                        val checkBox_params = checkBox.layoutParams as? ConstraintLayout.LayoutParams
                        if (checkBox_params != null) {
                            Log.e("checkBox_params",checkBox_params.toString())
                            checkBox_params.setMargins(30)
                        }
                        if (checkBox_params != null) {
                            checkBox_params.startToStart = tv_sideDish.id
                        }
                        if(i.key == "1"){
                            if (checkBox_params != null) {
                                checkBox_params.topToBottom = tv_sideDish.id
                            }
                        }else{
                            val temp  = i.key?.toInt()?.minus(1)
                            if (checkBox_params != null) {
                                //checkBox_params.topToBottom = getResources().getIdentifier("cb_meal_$temp", "id", activity?.getPackageName())
                                checkBox_params.topToBottom = recordLastCheckBoxId
                            }
                        }

                        val defaultDrawable = checkBox.buttonDrawable


                        for (i in sideDishList){
                            if(i.supply == true){
                                checkBox.text = sideDish.name
                                checkBox.isChecked = true
                                checkBox.setButtonDrawable(defaultDrawable)
                                checkBox.toggle()
                            }else{
                                checkBox.text = sideDish.name + " (售罄)"
                                checkBox.isChecked = false
                                checkBox.setButtonDrawable(R.drawable.ic_baseline_clear_24)
                                checkBox.toggle()
                            }
                        }

                        checkBox.setOnCheckedChangeListener { buttonView, ischecked ->
                            if (ischecked == true){
                                checkBox.text = sideDish.name + " (售罄)"
                                sideDishList[i.key?.toInt()!!-1].supply = false
                                //checkBox.buttonDrawable = ic_baseline_clear_24
                                checkBox.setButtonDrawable(R.drawable.ic_baseline_clear_24)
                            }else{
                                checkBox.text = sideDish.name
                                sideDishList[i.key?.toInt()!!-1].supply = true
                                checkBox.setButtonDrawable(defaultDrawable)
                            }

                        }

                        checkBox.requestLayout()
                        if (fragment_modify != null) {
                            fragment_modify.addView(checkBox)
                        }
                        Log.e("id",checkBox.id.toString())

                        recordLastCheckBoxId = checkBox.id
                    }
                    val btn_modify_params = btn_modify.layoutParams as ConstraintLayout.LayoutParams
                    btn_modify_params.topToBottom = recordLastCheckBoxId
                    Log.e("recordLastCheckBoxId",recordLastCheckBoxId.toString())
                    btn_modify.requestLayout()
                }
            }
        })

        btn_modify.setOnClickListener {
            for (i in mealList){
                val supplyValues = i.getSupply()

                val childUpdates = hashMapOf<String, Any>(
                    "supply" to supplyValues
                )
                mealRef.child((mealList.indexOf(i)+1).toString()).updateChildren(childUpdates)
            }
            for (i in sideDishList){
                val supplyValues = i.getSupply()

                val childUpdates = hashMapOf<String, Any>(
                    "supply" to supplyValues
                )
                sideDishRef.child((sideDishList.indexOf(i)+1).toString()).updateChildren(childUpdates)
            }
        }



    }

    fun str2int(str: String): Int{
        val n = str.length
        var result = 0
        str.forEach { c -> result = result * 10 + (c.code - 48) }
        return result
    }
}
data class Meal(
    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    var number: Int,
    @SerializedName("supply")
    var supply: Boolean

){
    @JvmName("getSupply1")
    @Exclude
    fun getSupply():Boolean{
        return supply
    }
}
data class SideDish(
    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    var number: Int,
    @SerializedName("supply")
    var supply: Boolean
){
    @JvmName("getSupply2")
    @Exclude
    fun getSupply():Boolean{
        return supply
    }
}


