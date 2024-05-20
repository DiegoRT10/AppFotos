
package com.example.appfotos.ui.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appfotos.R
import com.example.appfotos.models.RecuerdoModel
import com.example.appfotos.utils.CloudStorageManager
import com.example.inventory.FotoTopAppBar
import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object InicioDestination : NavigationDestination {
    override val route = "inicio"
    override val titleRes = R.string.screen_name_inicio
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    navigateToCreateImage: () -> Unit,
    navigateToCreaditos: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = false,
    modifier: Modifier = Modifier,
    viewModel: InicioViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lista by viewModel.items.collectAsState()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val filteredList = lista.filter { it.tema!!.nombre!!.contains(searchQuery.text, ignoreCase = true) }

    //variable para cambiar el estilo de lista
    var opcionList by remember { mutableStateOf(1) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                FotoTopAppBar(
                    title = stringResource(InicioDestination.titleRes),
                    canNavigateBack = canNavigateBack,
                    navigateUp = onNavigateUp,
                    scrollBehavior = scrollBehavior,
                    goToInfo = navigateToCreaditos
                )
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Buscar fotos por tema...") }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    Button(
                        onClick = {
                                  opcionList = 1
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Vista extensa")
                    }
                    Button(
                        onClick = {
                            opcionList = 2
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Lista")
                    }

                    Button(
                        onClick = {
                            opcionList = 3
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Cuadricula")
                    }
                }
            }
        },
        floatingActionButton = {
            Row {
                FloatingActionButton(
                    onClick = navigateToCreateImage,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(
                            end = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateEndPadding(LocalLayoutDirection.current)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.screen_name_inicio)
                    )
                }
            }
        }
    ) { innerPadding ->
        InicioBody(
            opcionList= opcionList,
            list = filteredList,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun InicioBody(
    opcionList:Int,
    list: List<RecuerdoModel>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val storage = CloudStorageManager(context)
    val scope = rememberCoroutineScope()
    if (list.isEmpty()) {
        SinFotosView()
    } else {

        when(opcionList){
            1->{
                LazyColumn(modifier = modifier) {
                    items(list) { recuerdo ->
                        RecuerdoCardView(
                            onDownload = {
                                scope.launch {
                                    storage.downloadImage(
                                        context = context,
                                        recuerdo.url ?: "",
                                        recuerdo.id ?: ""
                                    )
                                }
                            },
                            onDelete = {
                                scope.launch {
                                    storage.deleteImage(
                                        idUsuario = recuerdo.usuario?.id ?: "",
                                        idImage = recuerdo.id ?: "",
                                        urlImage = recuerdo.url ?: ""
                                    )
                                }
                            },
                            recuerdoModel = recuerdo,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
            2->{
                LazyColumn(modifier = modifier) {
                    items(list) { recuerdo ->
                        RecuerdoCardMini(
                            onDownload = {
                                scope.launch {
                                    storage.downloadImage(
                                        context = context,
                                        recuerdo.url ?: "",
                                        recuerdo.id ?: ""
                                    )
                                }
                            },
                            onDelete = {
                                scope.launch {
                                    storage.deleteImage(
                                        idUsuario = recuerdo.usuario?.id ?: "",
                                        idImage = recuerdo.id ?: "",
                                        urlImage = recuerdo.url ?: ""
                                    )
                                }
                            },
                            recuerdoModel = recuerdo,
                            modifier = Modifier.padding(8.dp)
                        )

                    }
                }
            }
            3->{
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier.padding(8.dp)
                ) {
                    items(list) { recuerdo ->
                        RecuerdoCardSimple(
                            onDownload = {
                                scope.launch {
                                    storage.downloadImage(
                                        context = context,
                                        recuerdo.url ?: "",
                                        recuerdo.id ?: ""
                                    )
                                }
                            },
                            onDelete = {
                                scope.launch {
                                    storage.deleteImage(
                                        idUsuario = recuerdo.usuario?.id ?: "",
                                        idImage = recuerdo.id ?: "",
                                        urlImage = recuerdo.url ?: ""
                                    )
                                }
                            },
                            recuerdoModel = recuerdo,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }




    }
}

@Composable
fun SinFotosView(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Card(
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(8.dp)
            ) {
                Text(text = "Aún no tienes guardada ninguna foto")
                Text(text = "Presiona + para crear un recuerdo")
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun InicioBodyPreview(){
    //InicioBody()
}
