package com.yenmin.ordermeal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class CartFragment(num: String) :Fragment(){
    var num = num
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var tv_meal = getView()?.findViewById<TextView>(R.id.tv_meal)
        var tv_sideDish = getView()?.findViewById<TextView>(R.id.tv_sideDish)
        val rv_meal = getView()?.findViewById<RecyclerView>(R.id.rv_meal)
        val rv_sideDish = getView()?.findViewById<RecyclerView>(R.id.rv_sideDish)
        val btn_sendOrder = getView()?.findViewById<Button>(R.id.btn_sendOrder)
        var tv_num = getView()?.findViewById<TextView>(R.id.tv_num)


        /*intent?.extras?.let{
            this.num = it.getString("num").toString()
            Log.e("num",this.num)
            tv_num.text = "桌號:"+this.num
        }*/

        val bundle = requireActivity().intent.extras
        val name = bundle!!.getString("num").toString()
        if (tv_num != null) {
            tv_num.text = "桌號:"+" "+this.num
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}