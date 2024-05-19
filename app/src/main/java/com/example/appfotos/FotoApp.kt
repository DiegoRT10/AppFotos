
package com.example.inventory

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appfotos.R
import com.example.appfotos.ui.inicio.InicioDestination
import com.example.appfotos.ui.navigation.FotoNavHost


@Composable
fun FotoApp(
    context: Context,
    navController: NavHostController = rememberNavController()
) {
    FotoNavHost(context,navController = navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FotoTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    goToInfo: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (title == stringResource(id = InicioDestination.titleRes)){
                IconButton(onClick = goToInfo) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Información"
                    )
                }
            }
        }

    )
}



