package com.example.simpsonsinfo.view.core.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simpsonsinfo.view.detail.CharacterScreen
import com.example.simpsonsinfo.view.MainPager
import com.example.simpsonsinfo.view.simpsons.SimpsonsViewModel

@Composable
fun NavigationWrapper(
    context: Context,
    simpsonsViewModel: SimpsonsViewModel,
    onSetDarkMode: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Main) {

        composable<Main> {
            MainPager(
                simpsonsViewModel = simpsonsViewModel,
                onNavigationDetail = {
                    navController.navigate(route = Character)
                },
                onSetDarkMode = { isDarkMode ->
                    onSetDarkMode(isDarkMode)
                }
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

//        composable<Simpsons> {
//            SimpsonsScreen(
//                simpsonsViewModel = simpsonsViewModel,
//                onNavigationDetail = {
//                    navController.navigate(route = Character)
//                }
//            )
//        }
//
//        composable<Locations> {
//            LocationScreen()
//        }
//
//        composable<Favourites> {
//            FavouritesScreen()
//        }
    }


}