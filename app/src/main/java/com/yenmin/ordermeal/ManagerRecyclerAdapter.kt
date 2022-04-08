package com.yenmin.ordermeal

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class ManagerRecyclerAdapter(
    private val data: ArrayList<Manager>,
    private val manager_map: MutableMap<String,String>
):RecyclerView.Adapter<ManagerRecyclerAdapter.ViewHolder>(){
    private lateinit var database: FirebaseDatabase
    private var managerMap = manager_map
    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val tv_number = v.findViewById<TextView>(R.id.number)
        val tv_name = v.findViewById<TextView>(R.id.name)
        val tv_position = v.findViewById<TextView>(R.id.position)
        val btn_del_manager = v.findViewById<Button>(R.id.del_manager)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_row_manager, parent, false)
        return ViewHolder(v)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        database = FirebaseDatabase.getInstance()
        holder.tv_number.text = data[position].id
        holder.tv_name.text = data[position].name
        holder.tv_position.text = data[position].position
        holder.btn_del_manager.setBackgroundColor(Color.parseColor("#ff0000"))
        //設定監聽器，使用 removeAt()刪除指定位置的資料
        holder.btn_del_manager.setOnClickListener {
            try {
                database.getReference("manager").child(data[position].key).removeValue()
                managerMap.remove(data[position].id)

            }catch (e: Exception){
                database.getReference("manager").child(data[position].key).removeValue()
                managerMap.remove(data[position].id)
            }finally {
                data.removeAt(position)
                notifyDataSetChanged()
            }


        }


    }



}