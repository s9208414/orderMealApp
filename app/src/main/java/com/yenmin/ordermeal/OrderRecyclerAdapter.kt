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
        holder.tv_number.text = data[position].number
        //holder.tv_meal.text = data[position].meal.toString()
        var tempMeal = ""
        for (i in 0 until data[position].meal.size){
            if (i % 2 != 0){
                //holder.tv_meal.text += data[position].meal.toString() + "\n"
                //holder.tv_meal.text += data[position].meal.toString() + "\n"
                tempMeal += data[position].meal[i].toString() + "\n"
            }else{
                tempMeal += data[position].meal[i].toString() + "X "
            }
        }
        holder.tv_meal.text = tempMeal
        //holder.tv_sideDish.text = data[position].sideDish.toString()
        var tempSideDish = ""
        for (i in 0 until data[position].sideDish.size){
            if (i % 2 != 0){
                //holder.tv_meal.text += data[position].meal.toString() + "\n"
                //holder.tv_meal.text += data[position].meal.toString() + "\n"
                tempSideDish += data[position].sideDish[i].toString() + "\n"
            }else{
                tempSideDish += data[position].sideDish[i].toString() + "X "
            }
        }
        holder.tv_sideDish.text = tempSideDish
        holder.btn_send_cooked.text = "送出餐點"
        //設定監聽器，使用 removeAt()刪除指定位置的資料
        holder.btn_send_cooked.setOnClickListener {
            data[position].cooked = true
            val childUpdates = hashMapOf<String, Any>(
                "cooked" to true
            )
            database.getReference("temp_order").child(data[position].number).updateChildren(childUpdates)
            data.removeAt(position)
            notifyDataSetChanged()
        }


    }



}