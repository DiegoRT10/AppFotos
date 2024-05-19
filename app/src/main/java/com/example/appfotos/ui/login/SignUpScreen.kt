package com.example.appfotos.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appfotos.R
import com.example.appfotos.ui.theme.Purple40
import com.example.appfotos.utils.AuthManager
import com.example.appfotos.utils.AuthRes
import com.example.inventory.FotoTopAppBar
import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object SignUpDestination : NavigationDestination {
    override val route = "signUp"
    override val titleRes = R.string.screen_name_registrarse
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    auth: AuthManager,
    navigation: NavHostController,
    onNavigateUp: () -> Unit) {

    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            FotoTopAppBar(
                title = stringResource(SignUpDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        }) {innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
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
            }
            Spacer(modifier = Modifier.height(30.dp))
            TextField(
                label = { Text(text = "Nombre usuario") },
                value = nombre,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = { nombre = it }
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Correo electronico") },
                value = email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = { email = it }
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Contraseña") },
                value = password,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password = it }
            )
            Spacer(modifier = Modifier.height(30.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        scope.launch {
                            signUp(email, password,nombre, auth, context, navigation)
                        }
                    },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Registrarse".uppercase())
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            ClickableText(
                text = AnnotatedString("¿Ya tienes una cuenta? Inicia sesión"),
                onClick = {
                    navigation.popBackStack()
                },
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Black
                )
            )
        }
    }
}

private suspend fun signUp(email: String, password: String, nombre:String, auth: AuthManager, context: Context, navigation: NavHostController) {
if (email.isNotEmpty() && password.isNotEmpty()){
    when(val result = auth.createUserWithEmailAndPassword(email, password,nombre)){
        is AuthRes.Success -> {
            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
            navigation.popBackStack()
        }
        is AuthRes.Error -> {
            Toast.makeText(context, "Error al registrar: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}else {
    Toast.makeText(context,"Existen campos vacios", Toast.LENGTH_SHORT).show()
}
}
