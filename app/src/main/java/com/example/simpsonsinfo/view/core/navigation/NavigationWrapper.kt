package com.example.simpsonsinfo.view.core.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simpsonsinfo.view.simpsons.CharacterScreen
import com.example.simpsonsinfo.view.simpsons.MainPager
import com.example.simpsonsinfo.view.simpsons.SimpsonsViewModel

@Composable
fun NavigationWrapper(context: Context, simpsonsViewModel: SimpsonsViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Main) {

        composable<Main> {
            MainPager(
                simpsonsViewModel = simpsonsViewModel,
                onNavigationDetail = {
                    navController.navigate(route = Character)
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