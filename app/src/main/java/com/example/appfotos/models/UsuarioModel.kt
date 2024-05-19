package com.example.appfotos.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UsuarioModel(
    val id: String? = null,
    val nombre:String? = null
) {
    constructor() : this("", "")
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "nombre" to nombre,
        )
    }
}