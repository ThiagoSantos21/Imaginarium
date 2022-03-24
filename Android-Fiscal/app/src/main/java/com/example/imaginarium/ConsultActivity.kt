package com.example.imaginarium

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialTextInputPicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ConsultActivity : AppCompatActivity() {

    private lateinit var etPlaca: TextInputEditText
    private lateinit var btConsultar: AppCompatButton
    private lateinit var tvStatus: AppCompatTextView
    private lateinit var cardInfo: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consult)

        etPlaca = findViewById(R.id.etPlaca)
        btConsultar = findViewById(R.id.btConsultar)
        tvStatus = findViewById(R.id.tvStatus)
        cardInfo = findViewById(R.id.cardInfo)

        btConsultar.setOnClickListener(){
            showResult()
        }
    }

    private fun showResult(){
        val teste = "ABC1234"
        if(etPlaca.text.isNullOrEmpty()){
            Snackbar.make(tvStatus, "Informe a placa", Snackbar.LENGTH_LONG).show()
        }
        if(etPlaca.text.toString() == teste){
            tvStatus.text = "Veiculo encontrado"
            tvStatus.setTextColor(Color.parseColor("#0CE315"))
            tvStatus.visibility = View.VISIBLE
            cardInfo.visibility = View.VISIBLE
        }
        else{
            tvStatus.text = "Veiculo nao encontrado"
            tvStatus.setTextColor(Color.parseColor("#FF0303"))
            tvStatus.visibility = View.VISIBLE
        }
    }
}