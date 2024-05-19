
package com.example.appfotos.ui.inicio

import androidx.lifecycle.ViewModel
import com.example.appfotos.models.RecuerdoModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InicioViewModel() : ViewModel() {

    //variable para base de datos
    private lateinit var database: DatabaseReference

    //lista de elementos
    private val _items = MutableStateFlow<List<RecuerdoModel>>(emptyList())
    val items: StateFlow<List<RecuerdoModel>> = _items


    private val itemsListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val itemList = mutableListOf<RecuerdoModel>()
            for (itemSnapshot in snapshot.children) {
                val item = itemSnapshot.getValue(RecuerdoModel::class.java)
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
        database.child("Recuerdo").addValueEventListener(itemsListener)
    }


    override fun onCleared() {
        super.onCleared()
        database.child("Recuerdo").removeEventListener(itemsListener)
    }
}



