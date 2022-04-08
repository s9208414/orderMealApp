package com.yenmin.ordermeal

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


class LoginActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var managerRef:DatabaseReference
    private lateinit var customerRef:DatabaseReference
    lateinit var employeeId: String
    var choose_login = false
    lateinit var btn_sendNum:Button
    lateinit var et_name:EditText
    lateinit var et_employee_num:EditText
    lateinit var btn_customer:Button
    lateinit var btn_manager:Button
    lateinit var tv_welcome:TextView
    lateinit var btn_employee_sendNum:Button
    lateinit var customerList: MutableList<Customer>
    private lateinit var mealRef: DatabaseReference
    //店家欄位
    var id = ""
    var position = ""
    //客人欄位
    var name = ""
    var number = 0
    var phone = ""
    var key = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_sendNum = findViewById<Button>(R.id.btn_sendNum)
        et_name = findViewById<EditText>(R.id.et_name)
        btn_customer = findViewById<Button>(R.id.btn_customer)
        btn_manager = findViewById<Button>(R.id.btn_manager)
        tv_welcome = findViewById<TextView>(R.id.tv_welcome)
        et_employee_num = findViewById<EditText>(R.id.et_employee_num)
        btn_employee_sendNum = findViewById<Button>(R.id.btn_employee_sendNum)

        var intent = Intent(this,MainActivity::class.java)
        et_name.visibility = View.GONE
        btn_sendNum.visibility = View.GONE
        btn_employee_sendNum.visibility = View.GONE
        et_employee_num.visibility = View.GONE

        val tv_welcome_params = tv_welcome.layoutParams as ConstraintLayout.LayoutParams
        tv_welcome_params.bottomToTop = btn_customer.id
        tv_welcome_params.topMargin = 520
        tv_welcome.requestLayout()

        database = FirebaseDatabase.getInstance()
        //dbRef = database.getReference("manager").child("1").child("id")
        managerRef = database.getReference("manager")
        customerRef = database.getReference("customer")
        FirebaseApp.initializeApp(this)

        var managerList = mutableListOf<Manager>()
        customerList = mutableListOf<Customer>()
        //設定按下顧客進入按鈕產生的UI
        btn_customer.setOnClickListener {
            tv_welcome_params.bottomToTop = et_name.id
            tv_welcome_params.topMargin = 0
            tv_welcome.requestLayout()
            btn_customer.visibility = View.GONE
            btn_manager.visibility = View.GONE
            et_name.visibility = View.VISIBLE
            btn_sendNum.visibility = View.VISIBLE
            choose_login = true
            et_name.text.clear()
        }
        //設定按下店家進入按鈕產生的UI
        btn_manager.setOnClickListener {
            tv_welcome_params.bottomToTop = et_employee_num.id
            tv_welcome_params.topMargin = 0
            tv_welcome.requestLayout()
            btn_customer.visibility = View.GONE
            btn_manager.visibility = View.GONE
            et_employee_num.visibility = View.VISIBLE
            btn_employee_sendNum.visibility = View.VISIBLE
            choose_login = true
            et_employee_num.text.clear()
        }

        btn_sendNum.setOnClickListener {
            var customerLogin = false
            if(et_name.length()<1){
               Toast.makeText(this,"請輸入姓名",Toast.LENGTH_SHORT).show()
            }else{
                //intent.putExtra("num",et_name.text.toString())
                //startActivity(intent)
                if (customerList.isEmpty()){
                    Toast.makeText(this,"請先到櫃台訂位",Toast.LENGTH_SHORT).show()
                }
                for (i in customerList){
                    if(et_name.text.toString() == i.name){
                        customerLogin = true
                        name = i.name
                        number = i.number
                        phone = i.phone
                        break

                    }else{
                        customerLogin = false
                        if (customerList.indexOf(i) == customerList.lastIndex){
                            Toast.makeText(this,"請先到櫃台訂位",Toast.LENGTH_SHORT).show()
                        }
                        continue
                    }


                }

                Log.e("133customerList",customerList.toString())
                Log.e("customerLogin",customerLogin.toString())
                Log.e("number", number.toString())
                if(customerLogin == true){
                    intent = Intent(this,MainActivity::class.java)
                    intent.putExtra("name",name)
                    intent.putExtra("number",number)
                    //intent.putExtra("bundle",b)
                    startActivity(intent)
                }
            }

        }
        customerRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(i in dataSnapshot.children){
                        val customer = Gson().fromJson(i.value.toString(), Customer::class.java)
                        customer.key = i.key.toString()
                        customerList.add(customer)
                        //Log.e("Value is",i.value.toString())
                        //managerList.add(Manager(i.value.))
                    }
                    Log.e("159customerList",customerList.toString())
                    //employeeId = dataSnapshot.getValue<String>().toString()

                    //Log.e("Value is",employeeId.toString())
                }else{
                    customerList.clear()
                }
            }
        })
        btn_employee_sendNum.setOnClickListener {
            var emplyeeLogin = false
            if(et_employee_num.length()<1){
                Toast.makeText(this,"請輸入員工號碼",Toast.LENGTH_SHORT).show()
            }else{
                for (i in managerList){
                    Log.e("et_employee_num",et_employee_num.toString())
                    Log.e("et_employee_num.text",et_employee_num.text.toString())
                    if(et_employee_num.text.toString() == i.id){
                        emplyeeLogin = true
                        id = i.id
                        position = i.position
                        break

                    }else{
                        Log.e("員工","未偵測到登入")
                        emplyeeLogin = false
                        if (managerList.indexOf(i) == managerList.lastIndex){
                            Toast.makeText(this,"請輸入正確員編",Toast.LENGTH_SHORT).show()
                        }
                        continue
                    }
                }

                Log.e("managerLogin",emplyeeLogin.toString())
                if(emplyeeLogin == true){

                    intent = Intent(this,ManagerActivity::class.java)
                    intent.putExtra("id",id)
                    intent.putExtra("position",position)
                    //intent.putExtra("bundle",b)
                    startActivity(intent)
                }
            }
        }
        managerRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(i in dataSnapshot.children){
                        val manager = Gson().fromJson(i.value.toString(), Manager::class.java)
                        managerList.add(manager)
                        //Log.e("Value is",i.value.toString())
                        //managerList.add(Manager(i.value.))
                    }
                    Log.e("ManagerList",managerList.toString())
                    //employeeId = dataSnapshot.getValue<String>().toString()

                    //Log.e("Value is",employeeId.toString())
                }else{
                    managerList.clear()
                }
            }
        })

        /*mealRef = database.getReference("sideDish")
        mealRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (i in dataSnapshot.children){
                        Log.e("Value is",i.toString())
                    }
                }
            }
        })*/

    }
    override fun onBackPressed() {
        if(choose_login == true){
            et_name.visibility = View.GONE
            btn_sendNum.visibility = View.GONE
            et_employee_num.visibility = View.GONE
            btn_employee_sendNum.visibility = View.GONE
            btn_customer.visibility = View.VISIBLE
            btn_manager.visibility = View.VISIBLE
            val tv_welcome_params = tv_welcome.layoutParams as ConstraintLayout.LayoutParams
            tv_welcome_params.bottomToTop = btn_customer.id
            tv_welcome_params.topMargin = 520
            tv_welcome.requestLayout()
            choose_login = false
        }else{
            super.onBackPressed()
            finish()
        }
    }
}
data class Manager(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("position")
    var position: String,
    var key: String
)
data class Customer(
    @SerializedName("number")
    var number: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("phone")
    var phone: String,
    var key: String
)