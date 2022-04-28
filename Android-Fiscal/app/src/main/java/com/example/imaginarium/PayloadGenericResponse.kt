package com.example.imaginarium

import com.google.gson.annotations.SerializedName
import com.google.firebase.Timestamp
import org.json.JSONObject

class PayloadGenericResponse {

        enum class StatusType(val type: String) {
            ERROR("ERROR"),
            SUCCESS("SUCCESS");
        }

        @SerializedName("placa")
        var placa: String? = null;
        @SerializedName("horaEntrada")
        var horaEntrada: Any? = null;
        @SerializedName("horaSaida")
        var horaSaida: Any? = null;

        override fun equals(other: Any?): Boolean {
            if (this === other)  return true
            if (javaClass != other?.javaClass) return false

            other as PayloadGenericResponse

            if (placa != other.placa) return false
            if (horaEntrada != other.horaEntrada) return false
            if (horaSaida != other.horaSaida) return false

            return true
        }

        override fun hashCode(): Int {
            var result = placa?.hashCode() ?: 0
            result = 31 * result + (horaEntrada?.hashCode() ?: 0)
            result = 31 * result + (horaSaida?.hashCode() ?: 0)
            return result
        }
}
