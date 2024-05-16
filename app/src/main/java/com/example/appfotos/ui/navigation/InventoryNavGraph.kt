
package com.example.inventory.ui.navigation

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
import com.example.inventory.ui.home.LoginDestination
import com.example.inventory.ui.home.LoginScreen



@Composable
fun FotoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        composable(route = LoginDestination.route) {
            LoginScreen(
                navigateToInicio = { navController.navigate(InicioDestination.route) },
            )
        }

        composable(route = InicioDestination.route) {
            InicioScreen(
                navigateToInfoImagen = { navController.navigate(InfoImagenDestination.route) },
                navigateToCreaditos = { navController.navigate(CreditosDestination.route) },
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
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
