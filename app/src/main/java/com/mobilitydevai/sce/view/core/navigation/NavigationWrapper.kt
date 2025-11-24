package com.mobilitydevai.sce.view.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobilitydevai.sce.view.detail.CharacterScreen
import com.mobilitydevai.sce.view.MainPager
import com.mobilitydevai.sce.view.auth.AuthViewModel
import com.mobilitydevai.sce.view.favorite.FavouriteScreen
import com.mobilitydevai.sce.view.simpsons.SimpsonsViewModel

@Composable
fun NavigationWrapper(
    darkThemeParam: Boolean,
    simpsonsViewModel: SimpsonsViewModel,
    authViewModel: AuthViewModel,
    onSetDarkMode: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Main) {

        composable<Main> {
            MainPager(
                simpsonsViewModel = simpsonsViewModel,
                authViewModel = authViewModel,
                darkThemeParam = darkThemeParam,
                onNavigationDetail = {
                    navController.navigate(route = Character)
                },
                onSetDarkMode = { isDarkMode ->
                    onSetDarkMode(isDarkMode)
                }
            )
        }

        composable<Favourite> {
            FavouriteScreen(
                simpsonsViewModel = simpsonsViewModel,
                onNavigationDetail = {
                    navController.navigate(route = Character)
                },
            )
        }

        composable<Character> {
            CharacterScreen(
                simpsonsViewModel = simpsonsViewModel,
                onBackNavigation = {
                    navController.popBackStack()
                }
            )
        }
    }
}