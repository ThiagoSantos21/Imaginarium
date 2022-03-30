package com.example.imaginarium

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import com.example.imaginarium.databinding.ActivityConsultBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialTextInputPicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ConsultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConsultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btConsultar.setOnClickListener(){
            showResult()
        }
        binding.btIrregular.setOnClickListener(){
            cameraPermission.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun showResult(){
        val teste = "ABC1234"
        if(binding.etPlaca.text.isNullOrEmpty()){
            Snackbar.make(binding.tvStatus, "Informe a placa", Snackbar.LENGTH_LONG).show()
        }
        else {
            if (binding.etPlaca.text.toString() == teste) {
                binding.tvStatus.text = "Veiculo encontrado"
                binding.tvStatus.setTextColor(Color.parseColor("#0CE315"))
                binding.btIrregular.visibility = GONE
                binding.tvStatus.visibility = VISIBLE
                binding.cardInfo.visibility = VISIBLE
            } else {
                binding.cardInfo.visibility = GONE
                binding.tvStatus.text = "Veiculo nao encontrado"
                binding.tvStatus.setTextColor(Color.parseColor("#FF0303"))
                binding.tvStatus.visibility = VISIBLE
                binding.btIrregular.visibility = VISIBLE
            }
        }
    }

    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                startPreview()
            }else{
                Snackbar.make(binding.tvStatus, "Conceda permissão para a camera", Snackbar.LENGTH_INDEFINITE).show()
            }
        }

    private fun startPreview() {
        val intent = Intent(this, CameraPreviewActivity::class.java)
        startActivity(intent)
    }
}