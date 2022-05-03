package com.example.imaginarium

import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

class ItineraryActivity : AppCompatActivity() {

    private lateinit var btDetalhes: MaterialButton
    private val places = arrayListOf<Place>(
        Place(LatLng(-22.910549, -47.060450),"Rua Álvares machado"),
        Place(LatLng(-22.909358, -47.061042),"Rua Cônego Cipião"),
        Place(LatLng(-22.909020, -47.060214),"Rua José de Alencar"),
        Place(LatLng(-22.911003, -47.059246),"Rua Gen Câmara"),
        Place(LatLng(-22.91071495186111, -47.058474404789614),"Rua José Paulinio")
    )

    private lateinit var googleMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itinerary)

        btDetalhes = findViewById(R.id.btDetails)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync{
            googleMap = it
            addMarkers(googleMap)
            googleMap.isMyLocationEnabled = true

            googleMap.setOnMapLoadedCallback {
                val bound = LatLngBounds.builder()

                places.forEach{
                    bound.include(it.latLng)
                }

                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bound.build(),200))
            }

            googleMap.addPolyline(PolylineOptions()
                .add(LatLng(-22.910549, -47.060450))
                .add(LatLng(-22.909358, -47.061042))
                .color(Color.BLUE))

            googleMap.addPolyline(PolylineOptions()
                .add(LatLng(-22.909358, -47.061042))
                .add(LatLng(-22.909020, -47.060214))
                .color(Color.GREEN))

            googleMap.addPolyline(PolylineOptions()
                .add(LatLng(-22.909020, -47.060214))
                .add(LatLng(-22.911003, -47.059246))
                .color(Color.RED))

            googleMap.addPolyline(PolylineOptions()
                .add(LatLng(-22.911003, -47.059246))
                .add(LatLng(-22.91071495186111, -47.058474404789614))
                .color(Color.BLACK))

            /*val URL = getDirectionURL(places[0].latLng,places[1].latLng)
            val result = GetDirection(URL)
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }
            googleMap.addPolyline(lineoption)*/
        }

        btDetalhes.setOnClickListener(){
            MaterialAlertDialogBuilder(this)
                .setTitle("Itinerário")
                .setMessage("Rua 1 : ${places[0].address} - 7:00 as 8:00\n" +
                        "Rua 2 : ${places[1].address} - 8:00 as 9:00\n" +
                        "Rua 3 : ${places[2].address} - 9:00 as 10:00\n" +
                        "Rua 4 : ${places[3].address} - 10:00 as 11:00\n" +
                        "Rua 5 : ${places[4].address} - 11:00 as 12:00")
                .setNeutralButton(
                    "OK"
                ) { dialog, which -> }
                .show()
        }


    }

    fun getDirectionURL(origin:LatLng,dest:LatLng):String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&key=AIzaSyCpGe9tvLJgzYdK4kWevYbG4re9f_zyuik"
    }

    fun GetDirection(url: String): List<List<LatLng>>{
            val client = OkHttpClient()
            val request = Request.Builder().url(url)
            val response = client.newCall(request.build()).execute()
            val data = response.toString()
            val result = ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data,GoogleMapDTO::class.java)
                Log.i("respObj",respObj.toString())
                val path = ArrayList<LatLng>()
                for(i in 0..(respObj.routes[0].legs[0].steps.size-1)){
                    val startlatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble(),
                        respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())
                    path.add(startlatLng)
                    val destlatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble(),
                        respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())
                    path.add(destlatLng)
                }
                result.add(path)

            }catch(e:Exception) {

                Log.i("ERROR", e.toString())
            }

            return result
        }

        /*val polylineOptions = PolylineOptions()
            .add(LatLng(37.35, -122.0))
            .add(LatLng(37.45, -122.0)) // North of the previous point, but at the same longitude
            .add(LatLng(37.45, -122.2)) // Same latitude, and 30km to the west
            .add(LatLng(37.35, -122.2)) // Same longitude, and 16km to the south
            .add(LatLng(37.35, -122.0))
        val polyline = mapFragment.addPolyline(polylineOptions)*/

    private fun addMarkers(googleMap: GoogleMap){
        places.forEach{ places->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(places.address)
                    .position(places.latLng)
            )
            val polylineOptions = PolylineOptions()
                .add(LatLng(places.latLng.latitude,places.latLng.longitude))

            val polyLine = googleMap.addPolyline(polylineOptions)
        }
    }
}




data class Place(
    val latLng: LatLng,
    val address: String,
)