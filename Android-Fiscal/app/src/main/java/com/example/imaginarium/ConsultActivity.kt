package com.example.imaginarium

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.imaginarium.databinding.ActivityConsultBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.google.type.Date
import java.text.SimpleDateFormat

class ConsultActivity : AppCompatActivity() {

    private var binding: ActivityConsultBinding = ActivityConsultBinding.inflate(layoutInflater)

    private var functions: FirebaseFunctions = Firebase.functions("southamerica-east1")

    private val logEntry = "PROCURA_PLACA";

    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btConsultar.setOnClickListener() {
            var placa = binding.etPlaca.toString()
            showResult(placa).addOnCompleteListener(OnCompleteListener{ task ->
                if (task.isSuccessful) {

                    val genericResp =
                        gson.fromJson(task.result, FunctionGenericResponse::class.java)

                    Log.i(logEntry, genericResp.status.toString())
                    Log.i(logEntry, genericResp.message.toString())
                    Log.i(logEntry, genericResp.payload.toString())

                    /*val insertInfo =
                        gson.fromJson(task.result.toString(), GenericInsertResponse::class.java)

                    Snackbar.make(
                        binding.btConsultar, "Placa encontrada: " + insertInfo.placa,
                        Snackbar.LENGTH_LONG
                    ).show();*/
                }
            })
            binding.btIrregular.setOnClickListener() {
                cameraPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun showResult(placa: String): Task<String> {

        val data = hashMapOf(
            "placa" to placa
        )

        return functions
            .getHttpsCallable("searchTicket")
            .call(data)
            .continueWith { task ->
                val res = gson.toJson(task.result?.data)
                res
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