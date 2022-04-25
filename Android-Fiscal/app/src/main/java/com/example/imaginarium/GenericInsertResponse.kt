package com.example.imaginarium

import com.google.gson.annotations.SerializedName

class GenericInsertResponse {

    @SerializedName("docId")
    var docId: String? = null;

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenericInsertResponse
        if (docId != other.docId) return false
        return true
    }

    override fun hashCode(): Int {
        var result = docId?.hashCode() ?: 0
        result = 31 * result + (docId?.hashCode() ?: 0)
        return result
    }

}