package com.example.appfotos.ui.login

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appfotos.R
import com.example.appfotos.ui.theme.Purple40
import com.example.appfotos.utils.AuthManager
import com.example.appfotos.utils.AuthRes
import com.example.inventory.FotoTopAppBar
import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ForgotPasswordDestination : NavigationDestination {
    override val route = "forgotPassword"
    override val titleRes = R.string.screen_name_forgotPassword
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    auth: AuthManager,
    navigation: NavController,
    onNavigateUp: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            FotoTopAppBar(
                title = stringResource(ForgotPasswordDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Por favor ingresa el correo que pertenece a tu cuenta donde recibiras un correo de recuperación de contraseña.",
                style = TextStyle(fontSize = 18.sp, color = Color.Black),
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(start = 35.dp, end = 35.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            TextField(
                label = { Text(text = "Correo electronico") },
                value = email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = { email = it }
            )
            Spacer(modifier = Modifier.height(30.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        scope.launch {
                            when (auth.resetPassword(email)) {
                                is AuthRes.Success -> {
                                    Toast.makeText(context, "Correo enviado", Toast.LENGTH_SHORT)
                                        .show()
                                    navigation.navigate(LoginDestination.route)
                                }

                                is AuthRes.Error -> {
                                    Toast.makeText(
                                        context,
                                        "Error al enviar el correo",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
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
                    Text(text = "Recuperar contraseña".uppercase())
                }
            }
        }
    }
}