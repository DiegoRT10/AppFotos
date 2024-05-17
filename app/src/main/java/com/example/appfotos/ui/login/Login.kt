
package com.example.inventory.ui.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appfotos.R
import com.example.inventory.ui.navigation.NavigationDestination
import androidx.navigation.NavController
import com.example.appfotos.ui.inicio.InicioDestination
import com.example.appfotos.utils.AuthManager
import com.example.appfotos.utils.AuthRes
import com.example.appfotos.ui.login.SignUpDestination
import com.example.appfotos.ui.theme.Purple40
import kotlinx.coroutines.launch


object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.screen_name_login
}
@Composable
fun LoginScreen(auth: AuthManager, navigation: NavController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box (modifier = Modifier.fillMaxSize()){
        ClickableText(
            text = AnnotatedString("¿No tienes una cuenta ? Registrate"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(50.dp),
            onClick = {
               navigation.navigate(SignUpDestination.route)
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            )
        )
    }
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        TextField(
            label = { Text(text = "Correo electronico")},
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = {email = it}
        )

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Contraseña")},
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {password = it}
        )

        Spacer(modifier = Modifier.height(30.dp))
        Box(modifier = Modifier.padding(40.dp,0.dp,40.dp,0.dp)){
            Button(
                onClick = {
                    scope.launch {
                        emailPassSignIn(email, password, auth,context,navigation)
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Iniciar sesión".uppercase())
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        ClickableText(
            text = AnnotatedString("¿Olvidaste tu contraseña?"),
            onClick = {

            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            )
        )
    }
}

private suspend fun emailPassSignIn(email: String, password: String, auth: AuthManager, context: Context, navigation: NavController) {
    if (email.isNotEmpty() && password.isNotEmpty()){
        when(val result = auth.signInWithEmailAndPassword(email, password)){
            is AuthRes.Success -> {
                navigation.navigate(InicioDestination.route) {
                    popUpTo(LoginDestination.route){
                        inclusive=true
                    }
                }
            }
            is AuthRes.Error -> {
                Toast.makeText(context, "Error al iniciar sesion: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }else {
        Toast.makeText(context,"Existen campos vacios", Toast.LENGTH_SHORT).show()
    }
}
