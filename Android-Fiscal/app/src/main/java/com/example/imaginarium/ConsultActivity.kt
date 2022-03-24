package com.example.imaginarium

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialTextInputPicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ConsultActivity : AppCompatActivity() {

    private lateinit var etPlaca: TextInputEditText
    private lateinit var btConsultar: MaterialButton
    private lateinit var tvStatus: AppCompatTextView
    private lateinit var cardInfo: MaterialCardView
    private lateinit var btIrregular: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consult)

        etPlaca = findViewById(R.id.etPlaca)
        btConsultar = findViewById(R.id.btConsultar)
        tvStatus = findViewById(R.id.tvStatus)
        cardInfo = findViewById(R.id.cardInfo)
        btIrregular = findViewById(R.id.btIrregular)

        btConsultar.setOnClickListener(){
            showResult()
        }
    }

    private fun showResult(){
        val teste = "ABC1234"
        if(etPlaca.text.isNullOrEmpty()){
            Snackbar.make(tvStatus, "Informe a placa", Snackbar.LENGTH_LONG).show()
        }
        else {
            if (etPlaca.text.toString() == teste) {
                tvStatus.text = "Veiculo encontrado"
                tvStatus.setTextColor(Color.parseColor("#0CE315"))
                btIrregular.visibility = View.GONE
                tvStatus.visibility = View.VISIBLE
                cardInfo.visibility = View.VISIBLE
            } else {
                cardInfo.visibility = View.GONE
                tvStatus.text = "Veiculo nao encontrado"
                tvStatus.setTextColor(Color.parseColor("#FF0303"))
                tvStatus.visibility = View.VISIBLE
                btIrregular.visibility = View.VISIBLE
            }
        }
    }
}