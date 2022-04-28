package com.example.imaginarium

import com.google.gson.annotations.SerializedName

class TimeGenericResponse {

    @SerializedName("_seconds")
    var seconds: Long? = null;
    @SerializedName("_nanoseconds")
    var nanoseconds: Int? = null;


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimeGenericResponse
        if (seconds != other.seconds) return false
        if (nanoseconds != other.nanoseconds) return false
        return true
    }

    override fun hashCode(): Int {
        var result = seconds?.hashCode() ?: 0
        result = 31 * result + (nanoseconds?.hashCode() ?: 0)
        return result
    }

}