package com.example.appfotos.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class RecuerdoModel(
    val id:String? = null,
    val url:String? = null,
    val tema: TemaModel? = null,
){
    constructor() : this("", "")
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "url" to url,
            "tema" to tema?.toMap()
        )
    }

}
