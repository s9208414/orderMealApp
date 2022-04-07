package com.yenmin.ordermeal

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class RecieveFragment(num_manger: String, position: String): Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var tempOrderRef: DatabaseReference
    private lateinit var orderadapter: OrderRecyclerAdapter
    private var temp_order_list = ArrayList<TempOrder>()
    private var temp_order_map = mutableMapOf<String,String>()
    var num = num_manger
    var position = position
    lateinit var decoration:RecyclerViewItemSpace
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        decoration = RecyclerViewItemSpace()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recieve,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var tv_num_manager = getView()?.findViewById<TextView>(R.id.tv_num_manager)
        var tv_position = getView()?.findViewById<TextView>(R.id.tv_position)
        val rv_order = getView()?.findViewById<RecyclerView>(R.id.rv_order)
        if (tv_num_manager != null) {
            tv_num_manager.text = "員工編號:"+" "+this.num
        }
        if (tv_position != null) {
            tv_position.text = "職位:"+" "+this.position
        }
        database = FirebaseDatabase.getInstance()
        tempOrderRef = database.getReference("temp_order")
        FirebaseApp.initializeApp(requireActivity())

        orderadapter = OrderRecyclerAdapter(temp_order_list)
        if (rv_order != null) {
            rv_order.addItemDecoration(decoration)
            rv_order.layoutManager = LinearLayoutManager(requireActivity())
            rv_order.adapter = orderadapter
        }

        tempOrderRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        Log.e("i.value",i.value.toString())
                        var temp = Gson().fromJson(i.value.toString(),Temp::class.java)
                        if (temp_order_map.containsKey(i.key)){
                            if (temp.cooked == false){
                                //var order = TempOrder(temp.id.toString(),temp.meal.getList(),temp.sideDish.getList(),temp.cooked)
                                try {
                                    temp_order_list[i.key?.toInt()!!].meal = temp.meal.getList()
                                    temp_order_list[i.key?.toInt()!!].sideDish = temp.sideDish.getList()
                                    temp_order_list[i.key?.toInt()!!].cooked = temp.cooked
                                }catch (e: Exception){

                                }
                            }
                        }else{
                            if (temp.cooked == false){
                                try {
                                    temp_order_map[i.key.toString()] = "true"
                                    var order = TempOrder(temp.id.toString(),temp.meal.getList(),temp.sideDish.getList(),temp.cooked)
                                    temp_order_list.add(order)
                                }catch (e: Exception){

                                }
                            }
                        }
                        orderadapter.notifyDataSetChanged()
                        Log.e("temp_order_list",temp_order_list.toString())
                    }
                    val b = Bundle()
                    b.putParcelableArrayList("temp_order_list",temp_order_list)
                    fragmentManager?.setFragmentResult("toCustomer", b)
                    val managerActivity = activity as ManagerActivity
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }
}
data class Temp(
    @SerializedName("cooked")
    var cooked: Boolean,
    @SerializedName("id")
    var id: String,
    @SerializedName("meal")
    var meal: MealNum,
    @SerializedName("order")
    var order: String,
    @SerializedName("sideDish")
    var sideDish: SideDishNum,
    @SerializedName("status")
    var status: String,
    @SerializedName("sum")
    var sum: Int
)
data class MealNum(
    @SerializedName("牛排")
    var beef: Int,
    @SerializedName("豬排")
    var pork: Int,
    @SerializedName("魚排")
    var fish: Int
){
    fun getList():List<String>{
        var list = mutableListOf<String>()
        list.add(0,"牛排")
        list.add(1,beef.toString())
        list.add(2,"豬排")
        list.add(3,pork.toString())
        list.add(4,"魚排")
        list.add(5,fish.toString())
        var tempList = list.toList()
        return  tempList
    }
}
data class SideDishNum(
    @SerializedName("沙拉")
    var salad: Int,
    @SerializedName("馬鈴薯")
    var potato: Int,
    @SerializedName("義大利麵")
    var spaghetti: Int,
    @SerializedName("玉米濃湯")
    var coreSoup: Int,
){
    fun getList():List<String>{
        var list = mutableListOf<String>()
        list.add(0,"沙拉")
        list.add(1,salad.toString())
        list.add(2,"馬鈴薯")
        list.add(3,potato.toString())
        list.add(4,"義大利麵")
        list.add(5,spaghetti.toString())
        list.add(6,"玉米濃湯")
        list.add(7,coreSoup.toString())
        var tempList = list.toList()
        return  tempList
    }
}

data class TempOrder(
    var number: String,
    var meal: List<String>,
    var sideDish: List<String>,
    var cooked: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(number)
        parcel.writeStringList(meal)
        parcel.writeStringList(sideDish)
        parcel.writeByte(if (cooked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TempOrder> {
        override fun createFromParcel(parcel: Parcel): TempOrder {
            return TempOrder(parcel)
        }

        override fun newArray(size: Int): Array<TempOrder?> {
            return arrayOfNulls(size)
        }
    }
}