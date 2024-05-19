package com.example.appfotos.ui.creacionImagen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appfotos.models.TemaModel


//logica para mostrar y seleccionar los temas
@Composable
fun TemaViewApp(
    modifier: Modifier = Modifier
){

}

@Composable
fun CrearTemaView(
    onSave: () -> Unit,
    viewModel: TemaViewModel = viewModel(),
    modifier: Modifier = Modifier
){
    Card(modifier = modifier) {
        Column(

            horizontalAlignment =  Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = "Crear un tema",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
            TextField(
                label = {
                    Text(
                        text = "Nombre del tema")
                },
                value =  viewModel.nombreTema,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = {viewModel.nombreTema = it}
            )

            Button(onClick = {
                viewModel.guardarTema()
                onSave()
            }) {
                Text(text = "Crear tema")
            }
        }
    }
}

@Composable
fun TemaCardView(
    selection:TemaModel,
    tema: TemaModel,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(start= 8.dp, end=8.dp)
        ) {
            RadioButton(
                selected =  tema.id == selection.id   ,
                onClick = onSave
            )
            Text(
                text = tema.nombre ?: ""
            )
        }

    }
}

@Composable
fun TemaListaView(
    viewModel: TemaViewModel = viewModel(),
    modifier: Modifier = Modifier
){
    val temas = viewModel.items.collectAsState(emptyList())
    //si la lista esta vacia mostramos un mensaje
    if (temas.value.isEmpty()){
        Column {
            Text(text = "Aun no tienes ningún tema para esta foto")
            Text(text = "Presiona en 'Crear tema' para agregar uno")
        }

    }else{
        LazyRow(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ){
            items(temas.value){tema ->
                TemaCardView(
                    selection = viewModel.temaSeleccion.value ?: TemaModel(),
                    tema = tema,
                    onSave = { viewModel.temaSeleccion.value = tema },
                    modifier.padding(8.dp))
            }
        }
    }

}


//dialogo para crear la vista
@Composable
fun DialogTema(
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
){
    Dialog(onDismissRequest ={onDismissRequest() } ){
        CrearTemaView(onSave = onSave)
    }
}

@Composable
@Preview(showSystemUi = false)
fun TemaViewAppPreview(){
    TemaViewApp()
}

@Composable
@Preview
fun CrearTemaViewPreview(){
    DialogTema(onDismissRequest = {}, onSave = {})
}


@Composable
@Preview
fun TemaCardPreview(
    modifier: Modifier = Modifier
){
    TemaCardView(selection = TemaModel("1","Prueba"),tema = TemaModel("1","Prueba"), onSave = {})
}
