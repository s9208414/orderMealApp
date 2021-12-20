package com.yenmin.ordermeal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btn_sendNum = findViewById<Button>(R.id.btn_sendNum)
        val et_num = findViewById<EditText>(R.id.et_num)
        var intent = Intent(this,MainActivity::class.java)
        btn_sendNum.setOnClickListener {
            if(et_num.length()<1){
               Toast.makeText(this,"請輸入桌號",Toast.LENGTH_LONG).show()
            }else{
                intent.putExtra("num",et_num.text.toString())
                startActivity(intent)
            }

        }
    }
}