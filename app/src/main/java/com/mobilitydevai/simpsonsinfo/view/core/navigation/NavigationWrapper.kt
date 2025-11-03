package com.mobilitydevai.simpsonsinfo.view.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobilitydevai.simpsonsinfo.view.detail.CharacterScreen
import com.mobilitydevai.simpsonsinfo.view.MainPager
import com.mobilitydevai.simpsonsinfo.view.favorite.FavouriteScreen
import com.mobilitydevai.simpsonsinfo.view.simpsons.SimpsonsViewModel

@Composable
fun NavigationWrapper(
    darkThemeParam: Boolean,
    simpsonsViewModel: SimpsonsViewModel,
    onSetDarkMode: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Main) {

        composable<Main> {
            MainPager(
                simpsonsViewModel = simpsonsViewModel,
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