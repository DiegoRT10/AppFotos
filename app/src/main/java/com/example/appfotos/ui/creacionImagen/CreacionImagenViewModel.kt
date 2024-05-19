
package com.example.appfotos.ui.creacionImagen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.appfotos.models.RecuerdoModel
import com.example.appfotos.models.TemaModel
import com.example.appfotos.models.UsuarioModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreacionImagenViewModel() : ViewModel() {

    //variables para guardar el id de la posible imagen
    var idImagen by mutableStateOf("")
    var usuario: MutableState<UsuarioModel?> = mutableStateOf(null)
    private lateinit var database: DatabaseReference

    fun guardarDataImagen(tema:TemaModel?){
        val recuerdoModel = RecuerdoModel(id = idImagen, tema = tema, usuario = usuario.value)
        database.child("Recuerdo").child(usuario.value?.id ?: "").child(idImagen).setValue(recuerdoModel.toMap())
    }

    //buscamos el usuario al conseguir el uid
    fun obtenerUsuario(idUsuario: String){
        database.child("Usuario").child(idUsuario).get().addOnSuccessListener {
            if (it.exists()){
                val data = it.getValue(UsuarioModel::class.java)
                usuario.value = data
            }
        }
    }

    init {
        database = FirebaseDatabase.getInstance().getReference()
        val key: String = database.push().key ?: ""
        idImagen = key
    }
}



