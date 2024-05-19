
package com.example.appfotos.ui.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appfotos.R
import com.example.inventory.ui.navigation.NavigationDestination
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.FotoTopAppBar


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

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FotoTopAppBar(
                title = stringResource(InicioDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior,
                goToInfo = navigateToCreaditos
            )
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

        },


    ) { innerPadding ->
        InicioBody(
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun InicioBody(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    SinFotosView()
}



@Composable
fun SinFotosView(
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Card(

            modifier = modifier
        )  {
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
    InicioBody()
}
