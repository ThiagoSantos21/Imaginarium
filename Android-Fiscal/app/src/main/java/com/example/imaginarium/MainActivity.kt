package com.example.imaginarium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.imaginarium.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btConsultar.setOnClickListener(){
            startConsult()
        }
        binding.btItinerario.setOnClickListener(){
            startItinerary()
        }

    }

    private fun startConsult() {
        val intent = Intent(this, ConsultActivity::class.java)
        startActivity(intent)

    }
    private fun startItinerary() {
        val intent = Intent(this, ItineraryActivity::class.java)
        startActivity(intent)

    }

}