package com.yenmin.ordermeal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class CustomerFragment(num_manger: String, position: String): Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var customerRef: DatabaseReference
    private lateinit var tempOrderRef: DatabaseReference
    private lateinit var customeradapter: CustomerRecyclerAdapter
    private var orderList = ArrayList<TempOrder>()
    private var customer_list = ArrayList<Customer>()
    private var customer_map = mutableMapOf<Int,String>()
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
        return inflater.inflate(R.layout.fragment_customer,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var tv_num_manager = getView()?.findViewById<TextView>(R.id.tv_num_manager)
        var tv_position = getView()?.findViewById<TextView>(R.id.tv_position)
        val rv_customer = getView()?.findViewById<RecyclerView>(R.id.rv_customer)
        var btn_add_customer = getView()?.findViewById<Button>(R.id.btn_add_customer)
        var et_num = getView()?.findViewById<EditText>(R.id.et_num)
        var et_name = getView()?.findViewById<EditText>(R.id.et_name)
        var et_phone = getView()?.findViewById<EditText>(R.id.et_phone)

        if (tv_num_manager != null) {
            tv_num_manager.text = "員工編號:"+" "+this.num
        }
        if (tv_position != null) {
            tv_position.text = "職位:"+" "+this.position
        }
        fragmentManager?.setFragmentResultListener("toCustomer",viewLifecycleOwner){ key,bundle ->
            try {
                orderList = bundle.getParcelableArrayList<TempOrder>("temp_order_list") as ArrayList<TempOrder>
                Log.e("orderList",orderList.toString())
            }catch (e: Exception){

            }
            Log.e("orderList",orderList.toString())
        }
        btn_add_customer?.setOnClickListener {
            if (et_num!!.length() < 1){
                Toast.makeText(requireActivity(),"請輸入桌號",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (Integer.parseInt(et_num.text.toString()) in customer_map.keys){
                Toast.makeText(requireActivity(),"此位置已有人",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (et_name!!.length() < 1){
                Toast.makeText(requireActivity(),"請輸入姓名",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (et_phone!!.length() < 1){
                Toast.makeText(requireActivity(),"請輸入電話",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val childUpdates = hashMapOf<String, Any>(
                "name" to et_name.text.toString(),
                "number" to Integer.parseInt(et_num.text.toString()),
                "phone" to et_phone.text.toString(),
            )
            var lastIdx = 0
            if (customer_list.isEmpty()){
                lastIdx = 0
            }else{
                lastIdx = customer_list.lastIndexOf(customer_list.last())+1
            }

            customerRef.child(lastIdx.toString()).updateChildren(childUpdates)

            val OrderchildUpdates = hashMapOf<String, Any>(
                "status" to "空桌"
            )

            tempOrderRef.child(et_num.text.toString()).updateChildren(OrderchildUpdates).addOnSuccessListener {
                Log.e("設定status","成功")
            }
        }

        database = FirebaseDatabase.getInstance()
        customerRef = database.getReference("customer")
        tempOrderRef = database.getReference("temp_order")
        FirebaseApp.initializeApp(requireActivity())

        customeradapter = CustomerRecyclerAdapter(customer_list,customer_map,orderList)
        if (rv_customer != null) {
            rv_customer.addItemDecoration(decoration)
            rv_customer.layoutManager = LinearLayoutManager(requireActivity())
            rv_customer.adapter = customeradapter
        }

        customerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        Log.e("i.key",i.key.toString())
                        var temp = Gson().fromJson(i.value.toString(),Customer::class.java)
                        temp.key = i.key.toString()
                        if (customer_map.containsKey(temp.number)){
                            try {
                                customer_list[Integer.parseInt(i.key.toString())].key = i.key.toString()
                                customer_list[Integer.parseInt(i.key.toString())].number = temp.number
                                customer_list[Integer.parseInt(i.key.toString())].name = temp.name
                                customer_list[Integer.parseInt(i.key.toString())].phone = temp.phone
                            }catch (e: Exception){

                            }

                        }else{
                            try {
                                customer_map[temp.number] = temp.name
                                customer_list.add(temp)
                            }catch (e: Exception){

                            }
                        }
                        if (et_num != null) {
                            et_num.text.clear()
                        }
                        if (et_name != null) {
                            et_name.text.clear()
                        }
                        if (et_phone != null) {
                            et_phone.text.clear()
                        }
                        Log.e("customerMap",customer_map.toString())
                        Log.e("customerList",customer_list.toString())
                        customeradapter.notifyDataSetChanged()

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }
}

