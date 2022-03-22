package com.yenmin.ordermeal

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.text.FieldPosition

class ModifyFragment(num_manger: String, position: String): Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var mealRef: DatabaseReference
    private lateinit var sideDishRef: DatabaseReference
    var num = num_manger
    var position = position
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
        Log.e("number",num)
        //Log.e("tv_num_manager",tv_num_manager.toString())
        if (tv_num_manager != null) {
            tv_num_manager.text = "員工編號:"+" "+this.num
        }
        if (tv_position != null) {
            tv_position.text = "職位:"+" "+this.position
        }
        database = FirebaseDatabase.getInstance()
        mealRef = database.getReference("meal")
        FirebaseApp.initializeApp(requireActivity())

        mealRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (i in dataSnapshot.children){
                        Log.e("Value is",i.toString())
                    }
                }
            }
        })


    }
}