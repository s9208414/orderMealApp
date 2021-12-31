package com.yenmin.ordermeal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.FieldPosition

class SideDishRecyclerAdapter(private val data: ArrayList<Order>):RecyclerView.Adapter<SideDishRecyclerAdapter.ViewHolder>(){
    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val tv_name = v.findViewById<TextView>(R.id.name)
        val tv_number = v.findViewById<TextView>(R.id.number)
        val img_delete = v.findViewById<ImageView>(R.id.img_delete)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
        ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_row, parent, false)
            return ViewHolder(v)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_name.text = data[position].name
        holder.tv_number.text = "X"+data[position].number.toString()
        //設定監聽器，使用 removeAt()刪除指定位置的資料
        holder.img_delete.setOnClickListener {
            data.removeAt(position)
            notifyDataSetChanged()
        }
    }

    /*fun addItem(text: String?) {
        // 為了示範效果，固定新增在位置3。若要新增在最前面就把3改成0
        data.add(data.size-1, text)
        notifyItemInserted(3)
    }*/
}