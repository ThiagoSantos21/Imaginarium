package com.example.imaginarium

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Time
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import com.example.imaginarium.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var gson: Gson
    private lateinit var functions: FirebaseFunctions
    private lateinit var ticket: Ticket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gson =  GsonBuilder().enableComplexMapKeySerialization().create()

        functions = Firebase.functions("southamerica-east1")

        binding.btConsultar.setOnClickListener(){
            if(binding.etPlaca.text.isNullOrEmpty()){
                Snackbar.make(binding.etPlaca, "Informe a placa", Snackbar.LENGTH_LONG).show()
            }
            else{
                var placa = binding.etPlaca.text.toString()

                searchTicket(placa).addOnCompleteListener(OnCompleteListener{ task ->
                    if (task.isSuccessful) {

                        var Entrada: Timestamp? = null
                        var Saida: Timestamp? = null
                        val result =
                            gson.fromJson(task.result, FunctionGenericResponse::class.java)

                        Log.i("Status", result.status.toString())
                        Log.i("Message", result.message.toString())

                        val payload =
                            gson.fromJson(result.payload.toString(), PayloadGenericResponse::class.java)

                        Log.i("Placa", payload.placa.toString())

                        val horaEntrada =
                            gson.fromJson(payload.horaEntrada.toString(), TimeGenericResponse::class.java)

                        if (horaEntrada != null) {
                            Entrada =
                                horaEntrada.seconds?.let { it1 -> horaEntrada.nanoseconds?.let { it2 ->
                                    Timestamp(it1,
                                        it2
                                    )
                                } }
                        }

                        val horaSaida =
                            gson.fromJson(payload.horaSaida.toString(), TimeGenericResponse::class.java)

                        if (horaSaida != null) {
                            Saida=
                                horaSaida.seconds?.let { it1 -> horaSaida.nanoseconds?.let { it2 ->
                                    Timestamp(it1,
                                        it2
                                    )
                                } }

                            if (Entrada != null && Saida != null) {
                                MaterialAlertDialogBuilder(this)
                                    .setTitle("${result.message.toString()}")
                                    .setMessage("Placa: ${payload.placa.toString()}\nEntrada: ${Entrada.toDate().toString()}\nSaida: ${Saida.toDate().toString()}")
                                    .setNeutralButton("OK"
                                    ) { dialog, which -> }
                                    .show()
                            }
                        }
                    }
                })
            }
        }
        binding.btItinerario.setOnClickListener(){
            startItinerary()
        }

    }

    private fun searchTicket(placa: String): Task<String> {

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


    private fun startItinerary() {
        val intent = Intent(this, ItineraryActivity::class.java)
        startActivity(intent)
    }

}