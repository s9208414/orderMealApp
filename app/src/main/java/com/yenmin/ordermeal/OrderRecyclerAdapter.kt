package com.yenmin.ordermeal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class OrderRecyclerAdapter(
    private val data: ArrayList<TempOrder>
):RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder>(){
    private lateinit var database: FirebaseDatabase
    var enable = true
    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val tv_number = v.findViewById<TextView>(R.id.number)
        val tv_meal = v.findViewById<TextView>(R.id.meal)
        val tv_sideDish = v.findViewById<TextView>(R.id.sideDish)
        val btn_send_cooked = v.findViewById<Button>(R.id.send_cooked)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_row_reciever, parent, false)
        return ViewHolder(v)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        database = FirebaseDatabase.getInstance()

        holder.tv_number.text = data[position].name
        holder.tv_meal.text = "X"+data[position].number.toString()
        //設定監聽器，使用 removeAt()刪除指定位置的資料
        holder.img_delete.setOnClickListener {
            //data.removeAt(position)
            //notifyDataSetChanged()
            if (enable == true){
                if (data[position].number >= 1){
                    --data[position].number
                }
                val childUpdates = hashMapOf<String, Any>(
                    "${data[position].name}" to data[position].number
                )
                database.getReference("temp_order").child(num).child("meal").updateChildren(childUpdates)
                notifyDataSetChanged()
                orderFragment.calSum()
            }

        }


    }



}