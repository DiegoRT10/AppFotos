
package com.example.appfotos.ui.creditos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appfotos.R
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.FotoTopAppBar


object CreditosDestination : NavigationDestination {
    override val route = "creditos"
    override val titleRes = R.string.screen_name_creditos
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditosScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FotoTopAppBar(
                title = stringResource(CreditosDestination.titleRes),
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
                    modifier = modifier.padding(top = 40.dp),
                    textAlign = TextAlign.Center
                )
            }
        },
    ) { innerPadding ->
        CreditosBody(
            modifier = modifier.padding(top=45.dp),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun CreditosBody(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val bulletPoints = listOf(
        "Descargar fotografías",
        "Subir fotografías desde tu cámara",
        "Subir fotografías desde tu galeria",
        "Categorizar fotografías",
        "Visualizar todas tus fotografías",
        "Guardar por siempre momentos importantes"
    )
   Column(
       modifier = Modifier
           .fillMaxSize()
           .padding(contentPadding)
           .verticalScroll(rememberScrollState()),
       verticalArrangement = Arrangement.Top,
       horizontalAlignment = Alignment.CenterHorizontally
   ){
       Image(
       painter = painterResource(id = R.drawable.logo_app),
       contentDescription = stringResource(id = R.string.app_name),
       modifier = Modifier.size(140.dp)
   )
       Text(
           text = "Donde los recuerdos nunca se pierden".uppercase(),
           fontSize = 13.sp,
           modifier = Modifier.padding(top = 8.dp),
           fontWeight = FontWeight.Bold,
           fontFamily = FontFamily.SansSerif
       )
       Text(
           text = stringResource(id = R.string.app_description),
           modifier=Modifier.padding(16.dp),
           fontSize = 18.sp,
           textAlign = TextAlign.Justify,
           fontFamily = FontFamily.Serif
       )
       BulletPointText(bulletPoints = bulletPoints)
       Text(
           text = "Equipo de desarrollo: ",fontSize = 18.sp,
           textAlign = TextAlign.Justify,
           fontFamily = FontFamily.Serif
       )
       PersonCard(photo = R.drawable.alejandro_rodas, name = "Edwin Alejandro Rodas Carranza")
       PersonCard(photo = R.drawable.elzer_villela, name = "Elzer Fernando Villela Granillo")
       PersonCard(photo = R.drawable.diego_ramos, name = "Diego Alexis Ramos Torres")
   }
}


@Composable
fun BulletPointText(bulletPoints: List<String>) {
    Column {
        bulletPoints.forEach { point ->
            Row {
                Text(
                    text = "•",
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 6.dp, start = 16.dp)
                )
                Text(
                    text = point,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif
                )
            }
        }
    }
}

@Composable
fun PersonCard(
    photo: Int,
    name: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(6.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp),
        ) {
            Image(
                painter = painterResource(photo),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = name,
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                modifier= Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun prevCreditos(){
    CreditosScreen(navigateBack = { /*TODO*/ }, onNavigateUp = { /*TODO*/ })
}

@Preview
@Composable
fun prevPersonCard(){
    PersonCard(photo = R.drawable.alejandro_rodas, name = "Edwin Alejandro Rodas Carranza")
}
