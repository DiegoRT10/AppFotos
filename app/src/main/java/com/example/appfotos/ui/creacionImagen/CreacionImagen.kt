
package com.example.appfotos.ui.creacionImagen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appfotos.R
import com.example.appfotos.models.TemaModel
import com.example.appfotos.utils.CloudStorageManager
import com.example.inventory.FotoTopAppBar
import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


object CreacionImagenDestination : NavigationDestination {
    override val route = "creacionImagen"
    override val titleRes = R.string.screen_name_creacionImagen
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreacionImagenScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    modifier: Modifier = Modifier,
    uid:String,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current
    val storage = CloudStorageManager(context)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FotoTopAppBar(
                title = stringResource(CreacionImagenDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        }

    ) { innerPadding ->
        val viewModelCreacion:CreacionImagenViewModel = viewModel()
        viewModelCreacion.obtenerUsuario(uid)
        CreacionImagenBody(
            viewModelCreacion = viewModelCreacion,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
            storage = storage,
            onBack = navigateBack
        )
    }
}

@Composable
private fun CreacionImagenBody(
    viewModelCreacion:CreacionImagenViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    storage: CloudStorageManager
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "io.appFotos.android_firebase"+".provider",file)
    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var showDialogDelete by remember { mutableStateOf(false) }
    val guardar = remember { mutableStateOf(true) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()){
        if (it){
            Toast.makeText(context,"Foto tomada y cargada en la nube", Toast.LENGTH_SHORT).show()
            capturedImageUri = uri
            guardar.value = false
        }else {
            Toast.makeText(context, "No se pudo tomar la foto $it", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { resultUri ->
        if (resultUri != null){
            capturedImageUri = resultUri
            guardar.value = false
        }else{
            Toast.makeText(context, "No se pudo cargar la imagen de galeria", Toast.LENGTH_SHORT).show()
        }

    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val readStorageGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false

        if (cameraGranted) {
            Toast.makeText(context, "Permiso de cámara autorizado", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }

        if (readStorageGranted) {
            Toast.makeText(context, "Permiso de almacenamiento autorizado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show()
        }
    }

    //para activar o desactivar el dialogo
    val openCreateDialog = remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageCardView(uri = capturedImageUri)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Elige como quieres subir tu imagen")

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ){
            Button(onClick = {
                val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if(permissionCheckResult == PackageManager.PERMISSION_GRANTED){
                    cameraLauncher.launch(uri)
                }else{
                    permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE))
                }
            }) {
                Text(text = "Tomar una foto")
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = {
                galleryLauncher.launch("image/*")

                val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    galleryLauncher.launch("image/*")
                } else {
                    permissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                }

            }) {
                Text(text = "Subir desde galeria")
            }

        }
        Divider()
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "Elige un tema para tu imagen")

        Spacer(modifier = Modifier.height(10.dp))

        val viewModel: TemaViewModel = viewModel()
        TemaListaView(viewModel = viewModel)

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            openCreateDialog.value = true
        }) {
            Text(text = "Crear tema")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Divider()
        Spacer(modifier = Modifier.height(25.dp))
        Button(
            onClick = {
            guardar.value = true
        },
            enabled = !guardar.value
        ) {
            Text(text = "Guardar recuerdo")
        }

        Spacer(modifier = Modifier.height(5.dp))
        when {
            guardar.value ->{
                capturedImageUri.let {
                    if (it != Uri.EMPTY){
                        SubirArchivoView(storage= storage,
                            fileName =  file.name,
                            fileUri =  it,
                            id = viewModelCreacion.idImagen,
                            idUsuario = viewModelCreacion.usuario.value?.id ?: "",
                            onFinish = {
                                viewModelCreacion.guardarDataImagen(viewModel.temaSeleccion.value)
                                guardar.value = false
                                onBack()
                            }
                        )
                    }

                }
            }
        }



    }

    //condicional para crear el dialogo
    when { openCreateDialog.value -> {
            DialogTema(
                onDismissRequest = {
                    openCreateDialog.value = false
                },
                onSave = {
                    openCreateDialog.value = false
                }
            )
        }
    }
    



}

@Composable
fun ImageCardView(
    uri: Uri?,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
    ){
        uri.let {
            AsyncImage(
                model = it,
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxSize()
            )
        }

    }

}

//cuadro de carga de imagen
@Composable
fun SubirArchivoView(
    fileUri: Uri,
    fileName: String,
    id:String,
    idUsuario:String,
    storage: CloudStorageManager,
    onFinish: () -> Unit
) {
    var isUploading by remember { mutableStateOf(false) }
    var uploadSuccess by remember { mutableStateOf<Boolean?>(null) }

    val context = LocalContext.current

    LaunchedEffect(key1 = fileUri) {
        isUploading = true
        uploadSuccess = try {
            storage.uploadFile(fileName, fileUri,id,idUsuario)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
        isUploading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isUploading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Subiendo...")
        } else {
            when (uploadSuccess) {
                true -> {
                    onFinish()
                }
                false -> Text(
                    text = "Algo salio mal intentalo denuevo.",
                    color = Color.Red
                )
                null -> {
                    Text(
                        text = "",
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun ImageCardViewPreview(){
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_"+timeStamp+"_"
    return File.createTempFile(imageFileName,".jpg",externalCacheDir)
}

