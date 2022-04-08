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

class ManagerFragment(num_manger: String, position: String): Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var managerRef: DatabaseReference
    private lateinit var customeradapter: CustomerRecyclerAdapter
    private var manager_list = ArrayList<Manager>()
    private var manager_map = mutableMapOf<String,String>()
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
        return inflater.inflate(R.layout.fragment_manager,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var tv_num_manager = getView()?.findViewById<TextView>(R.id.tv_num_manager)
        var tv_position = getView()?.findViewById<TextView>(R.id.tv_position)
        val rv_manager = getView()?.findViewById<RecyclerView>(R.id.rv_manager)
        var btn_add_manager = getView()?.findViewById<Button>(R.id.btn_add_manager)
        var et_num = getView()?.findViewById<EditText>(R.id.et_num)
        var et_name = getView()?.findViewById<EditText>(R.id.et_name)
        var et_position = getView()?.findViewById<EditText>(R.id.et_position)

        if (tv_num_manager != null) {
            tv_num_manager.text = "員工編號:"+" "+this.num
        }
        if (tv_position != null) {
            tv_position.text = "職位:"+" "+this.position
        }

        btn_add_manager?.setOnClickListener {
            if (et_num!!.length() < 1){
                Toast.makeText(requireActivity(),"請輸入員編",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (et_num.text.toString() in manager_map.keys){
                Toast.makeText(requireActivity(),"此員編重複，請輸入新員編",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (et_name!!.length() < 1){
                Toast.makeText(requireActivity(),"請輸入姓名",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (et_position!!.length() < 1){
                Toast.makeText(requireActivity(),"請輸入職位",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val childUpdates = hashMapOf<String, Any>(
                "name" to et_name.text.toString(),
                "id" to et_num.text.toString(),
                "position" to et_position.text.toString(),
            )
            var lastIdx = 0
            if (manager_list.isEmpty()){
                lastIdx = 0
            }else{
                lastIdx = manager_list.lastIndexOf(manager_list.last())+1
            }

            managerRef.child(lastIdx.toString()).updateChildren(childUpdates)


        }

        database = FirebaseDatabase.getInstance()
        managerRef = database.getReference("manager")
        FirebaseApp.initializeApp(requireActivity())

        manageradapter = CustomerRecyclerAdapter(manager_list,manager_map)
        if (rv_manager != null) {
            rv_manager.addItemDecoration(decoration)
            rv_manager.layoutManager = LinearLayoutManager(requireActivity())
            rv_manager.adapter = manageradapter
        }

        managerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        Log.e("i.key",i.key.toString())
                        var temp = Gson().fromJson(i.value.toString(),Manager::class.java)
                        temp.key = i.key.toString()
                        if (manager_map.containsKey(temp.id)){
                            try {
                                manager_list[Integer.parseInt(i.key.toString())].key = i.key.toString()
                                manager_list[Integer.parseInt(i.key.toString())].id = temp.id
                                manager_list[Integer.parseInt(i.key.toString())].name = temp.name
                                manager_list[Integer.parseInt(i.key.toString())].position = temp.position
                            }catch (e: Exception){

                            }

                        }else{
                            try {
                                manager_map[temp.id] = temp.name
                                manager_list.add(temp)
                            }catch (e: Exception){

                            }
                        }
                        if (et_num != null) {
                            et_num.text.clear()
                        }
                        if (et_name != null) {
                            et_name.text.clear()
                        }
                        if (et_position != null) {
                            et_position.text.clear()
                        }
                        Log.e("customerMap",manager_map.toString())
                        Log.e("customerList",manager_list.toString())
                        manageradapter.notifyDataSetChanged()

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }
}

