package com.yenmin.ordermeal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.FieldPosition

class MyRecyclerAdapter(private val data: ArrayList<String>):RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>(){
    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val name = v.findViewById<TextView>(R.id.name)
        val number = v.findViewById<TextView>(R.id.number)
    }

    override fun getItemCount() = data.size

    override fun onCreatViewHolder(viewGroup: ViewGroup, position: Int):
            ViewHolder{
                val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_row, viewGroup, false)
                return ViewHolder(v)
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }
}