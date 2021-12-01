package com.example.contactutils

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class IncomingCallActivity : AppCompatActivity() {
    var number: String? = null
    var text :TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefsAppUpdate: SharedPreferences =
            getSharedPreferences("filename", Context.MODE_PRIVATE)
        number = prefsAppUpdate.getString("number", "")

       setContentView(R.layout.dialog_call_log)

        text=findViewById(R.id.text)
        text!!.text=number



    }
}