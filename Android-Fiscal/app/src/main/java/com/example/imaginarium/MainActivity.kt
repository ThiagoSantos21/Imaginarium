package com.example.imaginarium

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.imaginarium.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
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
    private var ticket = Ticket("a",Timestamp(0,0), Timestamp(0,0))
    private val places = arrayListOf<Place>(
        Place(LatLng(-22.910549, -47.060450),"Rua Álvares machado"),
        Place(LatLng(-22.909358, -47.061042),"Rua Cônego Cipião"),
        Place(LatLng(-22.909020, -47.060214),"Rua José de Alencar"),
        Place(LatLng(-22.911003, -47.059246),"Rua Gen Câmara"),
        Place(LatLng(-22.91071495186111, -47.058474404789614),"Rua José Paulinio")
    )

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
                val placa = binding.etPlaca.text.toString()

                validateTicket(placa).addOnCompleteListener(OnCompleteListener{ task ->
                    if (task.isSuccessful) {

                        val result =
                            gson.fromJson(task.result, FunctionGenericResponse::class.java)

                        val payload =
                            gson.fromJson(result.payload.toString(), PayloadGenericResponse::class.java)

                        val horaEntrada =
                            gson.fromJson(payload.horaEntrada.toString(), TimeGenericResponse::class.java)
                        val horaSaida =
                            gson.fromJson(payload.horaSaida.toString(), TimeGenericResponse::class.java)

                        if(horaEntrada!=null && horaSaida!=null) {
                            ticket.placa = payload.placa.toString()
                            ticket.horaEntrada =
                                horaEntrada.seconds?.let { it1 ->
                                    horaEntrada.nanoseconds?.let { it2 ->
                                        Timestamp(it1, it2)
                                    }
                                }
                            ticket.horaSaida =
                                horaSaida.seconds?.let { it1 ->
                                    horaSaida.nanoseconds?.let { it2 ->
                                        Timestamp(it1, it2)
                                    }
                                }
                        }

                        when(result.status.toString()){
                            "NOTFOUND" ->
                                MaterialAlertDialogBuilder(this)
                                    .setTitle(result.message.toString())
                                    .setNegativeButton("Registrar", object : DialogInterface.OnClickListener{
                                        override fun onClick(p0: DialogInterface?, p1: Int) {
                                            startIrregularity()
                                        }

                                    })
                                    .setNeutralButton("Cancelar") { dialog, which -> }
                                    .show()
                            "ERROR" ->
                                MaterialAlertDialogBuilder(this)
                                    .setTitle(result.message.toString())
                                    .setMessage(
                                        "Placa: ${ticket.placa}\n" +
                                                "Entrada: ${ticket.horaEntrada?.toDate()?.hours}:${ticket.horaEntrada!!.toDate().minutes} | " +
                                                "${ticket.horaEntrada!!.toDate().day}/${ticket.horaEntrada!!.toDate().month}/${ticket.horaEntrada!!.toDate().year+ 1900}\n" +
                                                "Saida: ${ticket.horaSaida!!.toDate().hours}:${ticket.horaSaida!!.toDate().minutes} | " +
                                                "${ticket.horaSaida!!.toDate().day}/${ticket.horaSaida!!.toDate().month}/${ticket.horaSaida!!.toDate().year + 1900}"
                                    )
                                    .setNegativeButton("Registrar", object : DialogInterface.OnClickListener{
                                        override fun onClick(p0: DialogInterface?, p1: Int) {
                                            startIrregularity()
                                        }

                                    })
                                    .setNeutralButton("Cancelar") { dialog, which -> }
                                    .show()
                            "SUCCESS" ->
                                MaterialAlertDialogBuilder(this)
                                    .setTitle(result.message.toString())
                                    .setMessage(
                                        "Placa: ${ticket.placa}\n" +
                                        "Entrada: ${ticket.horaEntrada!!.toDate().hours}:${ticket.horaEntrada!!.toDate().minutes} | " +
                                                "${ticket.horaEntrada!!.toDate().day}/${ticket.horaEntrada!!.toDate().month}/${ticket.horaEntrada!!.toDate().year+ 1900}\n" +
                                        "Saida: ${ticket.horaSaida!!.toDate().hours}:${ticket.horaSaida!!.toDate().minutes} | " +
                                                "${ticket.horaSaida!!.toDate().day}/${ticket.horaSaida!!.toDate().month}/${ticket.horaSaida!!.toDate().year + 1900}"
                                    )
                                    .setNeutralButton(
                                        "OK"
                                    ) { dialog, which -> }
                                    .show()
                        }
                    }
                })
            }
        }
        binding.btItinerario.setOnClickListener(){
            startItinerary()
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment

        mapFragment.getMapAsync{ googleMap->
            addMarkers(googleMap)

            googleMap.setOnMapLoadedCallback {
                val bound = LatLngBounds.builder()

                places.forEach{
                    bound.include(it.latLng)
                }

                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bound.build(),200))
            }

            googleMap.addPolyline(
                PolylineOptions()
                .add(LatLng(-22.910549, -47.060450))
                .add(LatLng(-22.909358, -47.061042))
                .color(Color.BLUE))

            googleMap.addPolyline(
                PolylineOptions()
                .add(LatLng(-22.909358, -47.061042))
                .add(LatLng(-22.909020, -47.060214))
                .color(Color.GREEN))

            googleMap.addPolyline(
                PolylineOptions()
                .add(LatLng(-22.909020, -47.060214))
                .add(LatLng(-22.911003, -47.059246))
                .color(Color.RED))

            googleMap.addPolyline(
                PolylineOptions()
                .add(LatLng(-22.911003, -47.059246))
                .add(LatLng(-22.91071495186111, -47.058474404789614))
                .color(Color.BLACK))
        }

    }

    private fun addMarkers(googleMap: GoogleMap){
        places.forEach{ places->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .snippet(places.address)
                    .position(places.latLng)
            )
        }
    }

    private fun validateTicket(placa: String): Task<String> {

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

    private fun startIrregularity(){
        val intent = Intent(this, IrregularityActivity::class.java)
        startActivity(intent)
    }
    private fun startItinerary() {
        val intent = Intent(this, ItineraryActivity::class.java)
        startActivity(intent)
    }

}