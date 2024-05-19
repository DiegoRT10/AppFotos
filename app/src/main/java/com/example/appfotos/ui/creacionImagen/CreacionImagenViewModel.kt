
package com.example.appfotos.ui.creacionImagen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.appfotos.models.RecuerdoModel
import com.example.appfotos.models.TemaModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreacionImagenViewModel() : ViewModel() {

    //variables para guardar el id de la posible imagen
    var idImagen by mutableStateOf("")
    private lateinit var database: DatabaseReference


    fun guardarDataImagen(tema:TemaModel?){
        val recuerdoModel = RecuerdoModel(id = idImagen, tema = tema)
        database.child("Recuerdo").child(idImagen).setValue(recuerdoModel.toMap())
    }

    init {
        database = FirebaseDatabase.getInstance().getReference()
        val key: String = database.push().key ?: ""
        idImagen = key
    }
}



