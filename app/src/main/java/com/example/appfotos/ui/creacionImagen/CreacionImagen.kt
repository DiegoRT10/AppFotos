
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appfotos.R
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
    viewModel: CreacionImagenViewModel = viewModel()
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
        },
        floatingActionButton = {
            Row {

                Text(
                    text = "V.1.0.0",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = modifier.padding(top = 40.dp)
                )

            }

        },

    ) { innerPadding ->
        CreacionImagenBody(
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
            storage = storage
        )
    }
}

@Composable
private fun CreacionImagenBody(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    storage: CloudStorageManager
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "io.alejandro.android_firebase"+".provider",file)
    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var showDialogDelete by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()){
        if (it){
            Toast.makeText(context,"Foto tomada", Toast.LENGTH_SHORT).show()
            capturedImageUri = uri
            capturedImageUri?.let { uri ->
                scope.launch {
                    storage.uploadFile(file.name,uri)
                }
            }
        }else {
            Toast.makeText(context, "No se pudo tomar la foto $it", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Toast.makeText(context,"Permiso autorizado", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        }else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Elige como quieres subir tu imagen")
        Spacer(modifier = Modifier.height(25.dp))
        Button(onClick = {
            val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if(permissionCheckResult == PackageManager.PERMISSION_GRANTED){
                cameraLauncher.launch(uri)
            }else{
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text(text = "Capturar Imagen")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { }) {
            Text(text = "Cargar Imagen")
        }
    }

}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_"+timeStamp+"_"
    return File.createTempFile(imageFileName,".jpg",externalCacheDir)
}

