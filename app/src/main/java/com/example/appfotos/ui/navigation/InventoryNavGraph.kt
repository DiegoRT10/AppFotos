
package com.example.appfotos.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appfotos.ui.creacionImagen.CreacionImagenDestination
import com.example.appfotos.ui.creacionImagen.CreacionImagenScreen
import com.example.appfotos.ui.creditos.CreditosDestination
import com.example.appfotos.ui.creditos.CreditosScreen
import com.example.appfotos.ui.infoImagen.InfoImagenDestination
import com.example.appfotos.ui.infoImagen.InfoImagenScreen
import com.example.appfotos.ui.inicio.InicioDestination
import com.example.appfotos.ui.inicio.InicioScreen
import com.example.appfotos.ui.inicio.InicioViewModel
import com.example.appfotos.ui.login.ForgotPasswordDestination
import com.example.appfotos.ui.login.ForgotPasswordScreen
import com.example.appfotos.utils.AuthManager
import com.example.appfotos.ui.login.SignUpDestination
import com.example.appfotos.ui.login.SignUpScreen
import com.example.appfotos.ui.login.LoginDestination
import com.example.appfotos.ui.login.LoginScreen



@Composable
fun FotoNavHost(
    context: Context,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var authManager: AuthManager = AuthManager(context)
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        composable(route = LoginDestination.route) {
            LoginScreen(authManager,navController)
        }

        composable(route= SignUpDestination.route){
            SignUpScreen(
                auth = authManager,
                navigation = navController,
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(route= ForgotPasswordDestination.route){
            ForgotPasswordScreen(
                auth = authManager,
                navigation = navController,
                onNavigateUp = {navController.popBackStack()}
            )
        }

        composable(route = InicioDestination.route) {
            val viewModel = InicioViewModel(authManager.getCurrentUser()?.uid ?: "")
            InicioScreen(
                viewModel = viewModel,
                navigateToCreateImage = { navController.navigate(CreacionImagenDestination.route) },
                navigateToCreaditos = { navController.navigate(CreditosDestination.route) },
                onNavigateUp = { navController.navigateUp() },
                navigation = navController
            )
        }

        composable(route = InfoImagenDestination.route) {
            InfoImagenScreen(
                navigateToCreacionImagen = { navController.navigate(CreacionImagenDestination.route) },
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(route = CreacionImagenDestination.route) {
            CreacionImagenScreen(
                uid = authManager.getCurrentUser()?.uid ?: "",
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(route = CreditosDestination.route) {
            CreditosScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

    }
}
