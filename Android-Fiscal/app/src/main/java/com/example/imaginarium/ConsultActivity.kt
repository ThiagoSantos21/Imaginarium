package com.example.imaginarium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.snackbar.Snackbar

class ConsultActivity : AppCompatActivity() {

    private lateinit var etPlaca: AppCompatEditText
    private lateinit var btConsultar: AppCompatButton
    private lateinit var tvStatus: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consult)

        etPlaca = findViewById(R.id.etPlaca)
        btConsultar = findViewById(R.id.btConsultar)
        tvStatus = findViewById(R.id.tvStatus)

        btConsultar.setOnClickListener(){
            showResult()
        }
    }

    private fun showResult(){
        if(etPlaca.text.isNullOrEmpty()){
            Snackbar.make(tvStatus, "Informe a placa", Snackbar.LENGTH_LONG).show()
        }
        else{
            tvStatus.visibility = View.VISIBLE
        }
    }
}