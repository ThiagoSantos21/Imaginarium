package com.example.imaginarium.API

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoint {
    @GET()
    fun getTicket(@Path(value = "placa",encoded = true)placa : String): Call<JsonObject>


}