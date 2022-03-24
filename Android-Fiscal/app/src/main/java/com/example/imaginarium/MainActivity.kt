package com.example.imaginarium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    private lateinit var btConsultar: AppCompatButton
    private lateinit var btItinerario: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btConsultar = findViewById(R.id.btConsultar)
        btItinerario = findViewById(R.id.btItinerario)
        btConsultar.setOnClickListener(){
            val view = "ConsultActivity"
            startActivity()
        }
        btItinerario.setOnClickListener(){
            startActivity2()
        }

    }

    private fun startActivity() {
        val intent = Intent(this, ConsultActivity::class.java)
        startActivity(intent)

    }
    private fun startActivity2() {
        val intent = Intent(this, ItineraryActivity::class.java)
        startActivity(intent)

    }

}