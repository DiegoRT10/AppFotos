package com.example.appfotos.ui.creacionImagen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appfotos.models.TemaModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TemaViewModel : ViewModel() {


    //guardamos el nombre del tema
    var nombreTema by mutableStateOf("")

    //lista de elementos
    private val _items = MutableStateFlow<List<TemaModel>>(emptyList())
    val items: StateFlow<List<TemaModel>> = _items

    //guardar tema
    var temaSeleccion: MutableState<TemaModel?> = mutableStateOf(null)

    //variable para base de datos
    private lateinit var database: DatabaseReference


    private val itemsListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val itemList = mutableListOf<TemaModel>()
            for (itemSnapshot in snapshot.children) {
                val item = itemSnapshot.getValue(TemaModel::class.java)
                item?.let {
                    itemList.add(it)
                }
            }
            _items.value = itemList
        }

        override fun onCancelled(error: DatabaseError) {
            // Manejar el error de la consulta
        }
    }



    init {
        database = FirebaseDatabase.getInstance().getReference()
        database.child("Tema").addValueEventListener(itemsListener)
    }

    //funcion que guarda el tema
    fun guardarTema() : String{
        if (!nombreTema.trim().equals("")){
            //el nombre del tema no es nulo
            database = FirebaseDatabase.getInstance().getReference()
            val key: String = database.push().key ?: ""
            val tema = TemaModel(id = key, nombre = nombreTema )
            database.child("Tema").child(key).setValue(tema.toMap())
            nombreTema = ""
            return "Se inserto correctamente el tema"
        }

        //sin novedad devuelve un string vacio
        return ""
    }

    override fun onCleared() {
        super.onCleared()
        database.child("Tema").removeEventListener(itemsListener)
    }

}

