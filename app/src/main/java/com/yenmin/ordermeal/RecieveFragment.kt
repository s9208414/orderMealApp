package com.yenmin.ordermeal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class RecieveFragment(num_manger: String, position: String): Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var tempOrderRef: DatabaseReference
    private lateinit var orderadapter: OrderRecyclerAdapter
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
        return inflater.inflate(R.layout.fragment_recieve,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var tv_num_manager = getView()?.findViewById<TextView>(R.id.tv_num_manager)
        var tv_position = getView()?.findViewById<TextView>(R.id.tv_position)
        val rv_order = getView()?.findViewById<RecyclerView>(R.id.rv_order)
        if (tv_num_manager != null) {
            tv_num_manager.text = "員工編號:"+" "+this.num
        }
        if (tv_position != null) {
            tv_position.text = "職位:"+" "+this.position
        }
        database = FirebaseDatabase.getInstance()
        tempOrderRef = database.getReference("temp_order")
        FirebaseApp.initializeApp(requireActivity())
        orderadapter = OrderRecyclerAdapter()

        if (rv_order != null) {
            rv_order.addItemDecoration(decoration)
            rv_order.layoutManager = LinearLayoutManager(requireActivity())
            rv_order.adapter = orderadapter
        }
        tempOrderRef

    }
}
data class TempOrder(
    val number: String,
    val meal: List<String>,
    val sideDish: List<String>,
    val cooked: Boolean
)