package com.yenmin.ordermeal

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class CustomerRecyclerAdapter(
    private val data: ArrayList<Customer>
):RecyclerView.Adapter<CustomerRecyclerAdapter.ViewHolder>(){
    private lateinit var database: FirebaseDatabase
    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val tv_number = v.findViewById<TextView>(R.id.number)
        val tv_name = v.findViewById<TextView>(R.id.name)
        val tv_phone = v.findViewById<TextView>(R.id.phone)
        val btn_del_customer = v.findViewById<Button>(R.id.del_customer)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_row_customer, parent, false)
        return ViewHolder(v)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        database = FirebaseDatabase.getInstance()
        holder.tv_number.text = data[position].number.toString()
        holder.tv_name.text = data[position].name
        holder.tv_phone.text = data[position].phone
        holder.btn_del_customer.setBackgroundColor(Color.parseColor("#ff0000"))
        //設定監聽器，使用 removeAt()刪除指定位置的資料
        holder.btn_del_customer.setOnClickListener {
            database.getReference("customer").child(data[position].key).removeValue()
            data.removeAt(position)
            notifyDataSetChanged()
        }


    }



}