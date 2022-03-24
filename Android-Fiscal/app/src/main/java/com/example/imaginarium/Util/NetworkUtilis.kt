package com.example.imaginarium.Util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NetworkUtilis {
    companion object{
        fun getRetrofitInstance(path: String) : Retrofit {
            return Retrofit.Builder()
                .baseUrl(path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}