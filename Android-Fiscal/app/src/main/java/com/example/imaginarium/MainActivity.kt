package com.example.imaginarium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    private lateinit var btConsultar: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btConsultar = findViewById(R.id.btConsultar)
        btConsultar.setOnClickListener(){
            startActivity()
        }

    }

    private fun startActivity() {
        val intent = Intent(this, ConsultActivity::class.java)
        startActivity(intent)

    }

}